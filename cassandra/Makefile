STACK := hotelapp
IMAGE := cassandra:4.0.1
NET := perunanetti
TAG := 1.0

secret:
	echo '$(CASSANDRA_PASSWORD_MASTER)' | docker secret create $(STACK)-CASSANDRA_PASSWORD_MASTER -
	echo '$(CASSANDRA_PASSWORD_PAIR)' | docker secret create $(STACK)-CASSANDRA_PASSWORD_PAIR -

network:
	docker network create -d overlay --attachable perunanetti \
		--opt encrypted=true

volume:
	docker volume create cass_data

local:
	docker-compose -f cassandra.yml up

master:
	docker stack deploy --compose-file cassandra.yml $(STACK)

pair:
	docker stack deploy --compose-file second_node.yml $(STACK)

clean:
	docker stack rm $(STACK)

rmimg:
	docker rmi $(IMAGE):$(TAG)

.PHONY: clean build run run-dev run-prod rund pair master rming secret
