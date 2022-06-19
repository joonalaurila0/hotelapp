STACK_NAME := hotelapp
IMAGE_REPOSITORY := peruna/testi
IMAGES := cassandra:4.0.1 mariadb:10.3 vault:1.9.2
TIME := 3
SERVICES := config-server customer-service hotel-service

secret:
	echo '32' | docker secret create $(STACK_NAME)-MARIADB_PASSWORD - \
		&& echo 'cassandra' | docker secret create $(STACK_NAME)-CASSANDRA_PASSWORD -

network:
	docker network create -d overlay --attachable perunanetti \
		--opt encrypted=true

# Executes secret and network recipes
initialize: network secret casinit

install:
	docker pull $(IMAGES)

load:
	docker load < $(STACK_NAME).tar

build:
	docker build -t $(IMAGE_REPOSITORY):api -f api/Dockerfile .

# Deploy keycloak
keycloak:
	$(MAKE) deploy -C keycloak
	@echo "Waiting for the deployment to come up before Keycloak initialization..."
	sleep 10
	$(MAKE) init -C keycloak

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
	sleep 15
	docker stack deploy -c customer-service/customer-service.yml $(STACK_NAME)
	@echo "Waiting a moment for the state to converge..."
	sleep 15
	docker stack deploy -c hotel-service/hotel-service.yml $(STACK_NAME)
	@echo "Waiting a moment for the state to converge..."
	sleep 15
	docker stack deploy -c gateway/gateway.yml $(STACK_NAME)
	@echo "Waiting a moment for the state to converge..."

# Builds all the java services
build-java:
	cd config-server && gradle clean build
	$(MAKE) build -C config-server
	$(MAKE) run-detached -C config-server
	@echo "Configuration Server built and up."
	cd discovery && gradle clean build
	$(MAKE) build -C discovery
	$(MAKE) run-detached -C discovery
	@echo "Service Discovery built and up."
	cd customer-service && gradle clean build
	$(MAKE) build -C customer-service
	@echo "Customer service built."
	cd hotel-service && gradle clean build
	$(MAKE) build -C hotel-service
	@echo "Hotel service built."
	cd gateway && gradle clean build
	$(MAKE) build -C gateway
	$(MAKE) run-detached -C gateway
	@echo "Service Gateway built."
	$(MAKE) stop-detached -C config-server
	$(MAKE) stop-detached -C discovery
	$(MAKE) stop-detached -C gateway

#
# Deploys only the specified application
#
deploy-cfg:
	$(MAKE) deploy -C config-server

deploy-discovery:
	$(MAKE) deploy -C discovery

deploy-gateway:
	$(MAKE) deploy -C gateway

deploy-hotel:
	$(MAKE) deploy -C hotel-service

deploy-customer:
	$(MAKE) deploy -C customer-service

hcpvault:
	@echo "Initializing vault..."
	sh vault/startup.sh --swarm --auto-init

hcpvault-local:
	sh vault/startup.sh -c -i
	@echo "Vault deployed."

configuration-server:
	cd config-server && gradle clean build
	$(MAKE) build -C config-server
	cd config-server && $(MAKE) import
	@echo "Configuration Server built and imported."
	$(MAKE) deploy -C config-server

discovery:
	cd discovery && gradle clean build
	$(MAKE) build -C discovery
	cd discovery && $(MAKE) import
	@echo "Service Discovery built and imported."
	$(MAKE) deploy -C discovery

gateway:
	cd gateway && gradle clean build
	$(MAKE) build -C gateway
	cd gateway && $(MAKE) import
	@echo "Gateway built and imported."
	$(MAKE) deploy -C gateway

services:
	cd customer-service && gradle clean build
	$(MAKE) build deploy -C customer-service
	@echo "Customer service built."
	cd hotel-service && gradle clean build
	$(MAKE) build deploy -C hotel-service
	@echo "Hotel service built."

client:
	$(MAKE) build deploy -C client
	@echo "Client built and deployed."

casinit:
	$(MAKE) volume network -C cassandra

casmaster:
	@echo "Initializing master node for cassandra..."
	$(MAKE) master -C cassandra
	@echo "Sleeping 10 seconds so Swarm can converge its state..."
	sleep 10
	sh cassandra/startup.sh -h x220 --swarm --stack $(STACK) \
		--name cas-master --schema ${PWD}/cassandra/schema-init/schema.cql \
		--data ${PWD}/cassandra/schema-init/data.cql --database hotelapp

caspair:
	@echo "Initializing second node for cassandra..."
	$(MAKE) pair -C cassandra
	@echo "Sleeping 10 seconds so Swarm can converge its state..."
	sleep 10
	sh cassandra/startup.sh -h x220 --swarm --stack $(STACK) \
		--name cas-slave --schema ${PWD}/cassandra/schema-init/schema2.cql \
		--data ${PWD}/cassandra/schema-init/data2.cql --database hotelapp2

clean:
	@echo "Removing $(STACK_NAME) stack..."
	docker stack rm $(STACK_NAME)
	@echo "Sleeping $(TIME) seconds so Swarm can converge its state..."
	sleep $(TIME)
	@echo "Starting to clean docker state..."
	sh clean.sh

clear:
	docker secret rm $(STACK_NAME)-CASSANDRA_PASSWORD
	docker network prune -f
	docker image prune -f

.PHONY: clean clear initialize deploy build build-java load \
	install secret deploy-vault keycloak discovery casinit \
	casmaster caspair discovery configuration-server services \
	gateway client
