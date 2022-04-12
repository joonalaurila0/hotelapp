IMAGE := mariadb
TAG := 10.3
NET := perunanetti
SWARMHOST := 192.168.1.107

network:
	docker network create -d overlay --attachable perunanetti \
		--opt encrypted=true

volume:
	docker volume create mariadb_data

secret:
	echo '32' | docker secret create MARIADB_PASSWORD -

remote:
	docker stack deploy --compose-file mariadb.yml cluster-test

clean:
	docker rmi $(IMAGE):$(TAG)

.PHONY: clean network volume secret remote
