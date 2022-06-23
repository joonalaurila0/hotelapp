STACK_NAME := hotelapp
# Names of the servies
KAFKA_SERVICE := kafka
REDIS_SERVICE := redis

KAFKA_ID := $(shell docker ps -f "ancestor=quay.io/strimzi/kafka:0.29.0-kafka-3.2.0-amd64" -q)
REDIS_ID := $(shell docker ps -f "ancestor=redis:7.0.2-bullseye" -q)

network:
	docker network create -d overlay --attachable testinetti \
		--opt encrypted=true

pull:
	docker pull quay.io/strimzi/kafka:0.29.0-kafka-3.2.0-amd64

deploy:
	docker stack deploy --compose-file kafka.yml $(STACK_NAME)
	sleep 2
	docker stack deploy --compose-file redis.yml $(STACK_NAME)

deploy-kafka: kafka.yml
	docker stack deploy --compose-file kafka.yml $(STACK_NAME)

deploy-redis: redis.yml
	docker stack deploy --compose-file redis.yml $(STACK_NAME)

services:
	docker service ls

kafkalogs:
	docker service logs -f $(STACK_NAME)_kafka

redislogs:
	docker service logs -f $(STACK_NAME)_redis

topics:
	docker exec -t $(KAFKA_ID) ./bin/kafka-topics.sh --bootstrap-server=localhost:9092 --list

cache:
	docker exec -t $(REDIS_ID) redis-cli --scan --pattern '*'

flushcache:
	docker exec -t $(REDIS_ID) redis-cli FLUSHALL

init:
	docker exec -t $(KAFKA_ID) ./bin/kafka-topics.sh --bootstrap-server=localhost:9092 --create --topic hotel-service --replication-factor 1 --partitions 2
	docker exec -t $(KAFKA_ID) ./bin/kafka-topics.sh --bootstrap-server=localhost:9092 --create --topic customer-service --replication-factor 1 --partitions 2

clean:
	docker stack rm $(STACK_NAME)
	docker network rm testinetti

.PHONY: clean init topics logs logs2 deploy deploy2 pull network flushcache cache tests
