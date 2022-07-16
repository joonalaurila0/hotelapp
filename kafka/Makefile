STACK_NAME := hotelapp
KAFKA_SERVICE := kafka
KAFKA_ID := $(shell docker ps -f "ancestor=quay.io/strimzi/kafka:0.29.0-kafka-3.2.0-amd64" -q)

net:
	docker network create -d overlay --attachable testinetti \
		--opt encrypted=true

pull:
	docker pull quay.io/strimzi/kafka:0.29.0-kafka-3.2.0-amd64

deploy:
	docker stack deploy --compose-file kafka.yml $(STACK_NAME)
	sleep 2
	docker stack deploy --compose-file redis.yml $(STACK_NAME)

deploy: kafka.yml
	docker stack deploy --compose-file kafka.yml $(STACK_NAME)

kafkalogs:
	docker service logs -f $(STACK_NAME)_kafka

# ONLY WORKS FOR LOCAL DEPLOYMENT
topics:
	docker exec -t $(KAFKA_ID) ./bin/kafka-topics.sh --bootstrap-server=localhost:9092 --list

init:
	sh kafka-init.sh --docker-ctx x220 --swarm \
   --image quay.io/strimzi/kafka:0.29.0-kafka-3.2.0-amd64 \
   --stack hotelapp --name kafka --root $(PWD)

clean:
	docker stack rm $(STACK_NAME)
	docker network rm testinetti

.PHONY: clean init topics logs logs2 deploy deploy2 pull network flushcache cache tests
