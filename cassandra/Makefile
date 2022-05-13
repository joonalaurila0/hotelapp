IMAGE := cassandra:4.0.1
TAG := 1.0
NET := perunanetti
SWARMHOST := 192.168.1.107

network:
	docker network create -d overlay --attachable perunanetti \
		--opt encrypted=true

volume:
	docker volume create cass_data

local:
	docker-compose -d -f cassandra.yml up

remote:
	docker stack deploy --compose-file cassandra.yml cluster-test

clean:
	docker rmi $(IMAGE):$(TAG)

.PHONY: clean build run run-dev run-prod rund
