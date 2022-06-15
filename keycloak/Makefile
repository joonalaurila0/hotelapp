OG_IMAGE := quay.io/keycloak/keycloak
OG_TAG := 17.0.0
IMAGE := hotelapp/keycloak
TAG := 1.0
NET := perunanetti
SWARMHOST := 192.168.1.107

network:
	docker network create -d overlay --attachable perunanetti \
		--opt encrypted=true

volume:
	docker volume create keycloak_data

deploy:
	docker stack deploy --compose-file keycloak.yml hotelapp

init:
	sh startup.sh --host default -i quay.io/keycloak/keycloak:17.0.0 -s

build-with-mariadb:
	docker build -t $(IMAGE):$(TAG) -f Dockerfile \
		--build-arg KEYCLOAK_ADMIN=admin \
		--build-arg KEYCLOAK_ADMIN_PASSWORD=admin \
		--build-arg KC_DB=mariadb \
		--build-arg KC_DB_URL=jdbc:mariadb://localhost:3306/ \
		--build-arg KC_DB_USERNAME=root \
		--build-arg KC_DB_PASSWORD=32 \
		--build-arg KC_HOSTNAME=localhost:8080 \
		--build-arg KC_HTTP_ENABLED=true \
		--build-arg KC_HTTP_PORT=8080 \
		--build-arg KC_HOSTNAME_STRICT_HTTPS=false \
		--build-arg KC_METRICS_ENABLED=true \
		--build-arg KC_FEATURES=token-exchange \
		.
	@echo "Pruning intermediate images.."
	docker image prune -f

# Default
build:
	docker build -t $(IMAGE):$(TAG) -f Dockerfile \
		--build-arg KEYCLOAK_ADMIN=admin \
		--build-arg KEYCLOAK_ADMIN_PASSWORD=admin \
		--build-arg KC_HOSTNAME=$(SWARMHOST):8080 \
		--build-arg KC_HTTP_HOST=$(SWARMHOST):8080 \
		--build-arg KC_HTTP_ENABLED=true \
		--build-arg KC_HTTP_PORT=8080 \
		--build-arg KC_HOSTNAME_STRICT_HTTPS=false \
		--build-arg KC_METRICS_ENABLED=true \
		--build-arg KC_HEALTH_ENABLED=true \
		--build-arg KC_FEATURES=token-exchange \
		.
	@echo "Pruning intermediate images.."
	docker image prune -f


run:
	docker run --rm --name keycloak \
		-p 8443:8443 -p 8080:8080 --network $(NET) \
		$(IMAGE):$(TAG)

rundev:
	docker run --rm --name keycloak \
		-p 8080:8080 -p 8443:8443 \
		--network perunanetti \
		-e KEYCLOAK_ADMIN=admin \
		-e KEYCLOAK_ADMIN_PASSWORD=admin \
		--health-cmd='curl http://127.0.0.1:8080/health | exit 1' \
		--health-interval=15s \
		--health-timeout=10s \
		--health-retries=10 \
		--health-start-period=10s \
		$(OG_IMAGE):$(OG_TAG) \
		start-dev --help-all > helptext.txt

runprod:
	docker run --rm --name keycloak \
		-e KEYCLOAK_ADMIN=admin \
		-e KEYCLOAK_ADMIN_PASSWORD=admin \
		-p 8080:8080 --network $(NET) \
		$(OG_IMAGE):$(OG_TAG) \
		start \
		--auto-build

clean:
	docker rmi $(IMAGE):$(TAG)

.PHONY: clean build run run-dev run-prod rund
