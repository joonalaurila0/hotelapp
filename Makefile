STACK_NAME := hotelapp
SLEEP_TIME := 3

# Possible host used for deployment
# Define these as environment variables
# DOCKER_CTX_* variables are environment variables
# that are used for the Apache Cassandra deployment.
DOCKER_CTX_ONE := $(DOCKER_CTX_ONE)
DOCKER_CTX_TWO := $(DOCKER_CTX_TWO)

# Executes secret, volume and network recipes for Apache Cassandra
# NOTE: REMEMBER TO SOURCE .envs
initialize:
	@echo "Remember to source .envs!"
	$(MAKE) secret volume network -C cassandra

# Creates the network for the stack
network:
	docker network create -d overlay --attachable perunanetti \
		--opt encrypted=true

# Deploys and initializes Redis
redis:
	docker stack deploy --compose-file redis/redis.yml $(STACK_NAME)
	sleep 2
	$(MAKE) init -C redis

# Deploys and initializes Apache Kafka
kafka:
	docker stack deploy --compose-file kafka/kafka.yml $(STACK_NAME)
	sleep 10
	$(MAKE) init -C kafka

# Deploys and initializes Redhat Keycloak
keycloak:
	sh keycloak/startup.sh --host default --root $(PWD) \
		--stack $(STACK_NAME) --image quay.io/keycloak/keycloak:17.0.0 \
		--swarm

hcpvault:
	@echo "Initializing HashiCorp Vault..."
	sh vault/startup.sh --stack $(STACK_NAME) --swarm --auto-init --root $(PWD)

# Deploys elasticstack
elasticstack:
	$(MAKE) deploy -C elasticstack

# Builds, imports and deploys the configuration server
cfg:
	cd config-server && gradle clean build
	$(MAKE) build -C config-server
	cd config-server && $(MAKE) import
	@echo "Configuration Server built and imported."
	$(MAKE) deploy -C config-server

# Builds, imports and deploys the service registry
discovery:
	cd discovery && gradle clean build
	$(MAKE) build -C discovery
	cd discovery && $(MAKE) import
	@echo "Service Discovery built and imported."
	$(MAKE) deploy -C discovery

# Builds, imports and deploys the API Gateway
gateway:
	cd gateway && gradle clean build
	$(MAKE) build -C gateway
	cd gateway && $(MAKE) import
	@echo "Gateway built and imported."
	$(MAKE) deploy -C gateway

# Builds, imports and deploys the services (customer-service and hotel-service)
services:
	cd customer-service && gradle clean build
	$(MAKE) build import deploy -C customer-service
	@echo "Customer service built, imported and deployed."
	cd hotel-service && gradle clean build
	$(MAKE) build import deploy -C hotel-service
	@echo "Hotel service built, imported and deployed."

# Builds and deploys the client
client:
	$(MAKE) build deploy -C client
	@echo "Client built and deployed."

casmaster:
	@echo "Initializing master node for cassandra..."
	$(MAKE) master -C cassandra
	@echo "Sleeping 10 seconds so Swarm can converge its state..."
	sleep 10
	sh cassandra/startup.sh --docker-ctx $(DOCKER_CTX_ONE) \
		--swarm --stack $(STACK_NAME) --name cas-master --database hotelapp \
		--schema ${PWD}/cassandra/schema-init/schema.cql \
		--data ${PWD}/cassandra/schema-init/data.cql \
		--root ${PWD}

caspair:
	@echo "Initializing second node for cassandra..."
	$(MAKE) pair -C cassandra
	@echo "Sleeping 10 seconds so Swarm can converge its state..."
	sleep 10
	sh cassandra/startup.sh --docker-ctx $(DOCKER_CTX_ONE) \
		--swarm --stack $(STACK_NAME) --name cas-slave --database hotelapp2 \
		--schema ${PWD}/cassandra/schema-init/schema2.cql \
		--data ${PWD}/cassandra/schema-init/data2.cql\
		--root ${PWD}

# Prints environment variables for the remote hosts
inspect:
	@echo "SWARMHOST: $(SWARMHOST)"
	@echo "REMOTEHOST ONE: $(REMOTEHOST_ONE)"
	@echo "REMOTEHOST TWO: $(REMOTEHOST_TWO)"
	@echo "DOCKER CTX ONE: $(DOCKER_CTX_ONE)"
	@echo "DOCKER CTX TWO: $(DOCKER_CTX_TWO)"
	@echo "REMOTEHOST_LOCATION_TWO: $(REMOTEHOST_LOCATION_TWO)"

clean:
	@echo "Removing $(STACK_NAME) stack..."
	docker stack rm $(STACK_NAME)
	@echo "Sleeping $(SLEEP_TIME) seconds so Swarm can converge its state..."
	sleep $(SLEEP_TIME)
	@echo "Starting to clean docker state..."
	sh clean.sh

clear:
	docker secret rm $(STACK_NAME)-CASSANDRA_PASSWORD
	docker network prune -f
	docker image prune -f

.PHONY: clear clean caspair casmaster casinit \
	client services gateway discovery cfg \
	hcpvault keycloak hcpvault-local install \
	network secret initialize redis kafka elasticstack
