STACK_NAME := hotelapp
IMAGE_REPOSITORY := peruna/testi
IMAGES := cassandra:4.0.1 mariadb:10.3 vault:1.9.2
TIME := 3
SERVICES := config-server customer-service hotel-service

secret:
	echo '32' | docker secret create $(STACK_NAME)-MARIADB_PASSWORD - \
		&& echo 'cassandra' | docker secret create $(STACK_NAME)-CASSANDRA_PASSWORD -

clear:
	docker secret rm $(STACK_NAME)-MARIADB_PASSWORD
	docker secret rm $(STACK_NAME)-CASSANDRA_PASSWORD
	docker network prune -f

network:
	docker network create -d overlay --attachable perunanetti \
		--opt encrypted=true

install:
	docker pull $(IMAGES)

load:
	docker load < $(STACK_NAME).tar

build:
	docker build -t $(IMAGE_REPOSITORY):api -f api/Dockerfile .

# Deploys "external" services
deploy:
	docker stack deploy --compose-file hotelapp-deploy.yml $(STACK_NAME)
	@echo "Waiting for the deployment to come up before Vault initialization..."
	sleep $(TIME)
	@echo "Initializing vault..."
	sh vault/startup.sh --swarm --auto-init
	@echo "Initializing cassandra..."
	sh cassandra/startup.sh -h x220 -i cassandra:4.0.1 -f ${PWD}/cassandra/schema-init/init.cql --swarm

# Deploys all the java services
deploy-java:
	docker stack deploy -c config-server/config-server.yml $(STACK_NAME)
	@echo "Waiting a moment for the state to converge..."
	sleep 10
	docker stack deploy -c discovery/discovery.yml $(STACK_NAME)
	@echo "Waiting a moment for the state to converge..."
	sleep 10
	docker stack deploy -c customer-service/customer-service.yml $(STACK_NAME)
	@echo "Waiting a moment for the state to converge..."
	sleep 8
	docker stack deploy -c hotel-service/hotel-service.yml $(STACK_NAME)
	@echo "Waiting a moment for the state to converge..."
	sleep 5
	docker stack deploy -c gateway/gateway.yml $(STACK_NAME)
	@echo "Waiting a moment for the state to converge..."

# Builds all the java services
build-java:
	$(MAKE) mvnbuild -C config-server
	$(MAKE) build -C config-server
	$(MAKE) run-detached -C config-server
	@echo "Configuration Server built."
	$(MAKE) mvnbuild -C discovery
	$(MAKE) build -C discovery
	$(MAKE) run-detached -C discovery
	@echo "Service Discovery built."
	$(MAKE) mvnbuild -C customer-service 
	$(MAKE) build -C customer-service
	@echo "Customer service built."
	$(MAKE) mvnbuild -C hotel-service
	$(MAKE) build -C hotel-service
	@echo "Hotel service built."
	$(MAKE) mvnbuild -C gateway
	$(MAKE) build -C gateway
	$(MAKE) run-detached -C gateway
	@echo "Service Gateway built."
	$(MAKE) stop-detached -C config-server
	$(MAKE) stop-detached -C discovery
	$(MAKE) stop-detached -C gateway

# This is to test vault and configuration server
local-test:
	sh vault/startup.sh -c -i
	@echo "Vault deployed."
	$(MAKE) mvnbuild -C config-server
	$(MAKE) build -C config-server
	@echo "Deploying Configuration Server..."
	$(MAKE) run-detached -C config-server

local-dbtest:
	docker network create -d overlay --attachable perunanetti --opt encrypted=true
	cd mariadb && docker-compose up -d
	docker run --rm --name phpmyadmin -d -p 8080:80 --network perunanetti -e PMA_ARBITRARY=1 -e PMA_HOST=mariadb phpmyadmin:5.1.3-apache

local-keycloak:
	docker network create -d overlay --attachable perunanetti --opt encrypted=true

clean:
	@echo "Removing $(STACK_NAME) stack..."
	docker stack rm $(STACK_NAME)
	@echo "Sleeping $(TIME) seconds so Swarm can converge its state..."
	sleep $(TIME)
	@echo "Starting to clean docker state..."
	sh clean.sh

.PHONY: clean deploy build build-java load install secret deploy-vault
