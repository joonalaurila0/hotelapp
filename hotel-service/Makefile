APP := hotel-service
STACK := hotelapp
JAVA_IMAGE := hotelapp/hotel-service
JAR_FILE := hotel-service.jar
IMAGES := openjdk:17-slim-bullseye
TAG := 1.0
TIME := 3

# This variable is used for the import recipe.
# Specify the host you wish to import the image into.
# It is an environment variable, you can define it as 
# the ssh remote host you wish to use, if you're doing
# a multihost deployment. Example: myuser@192.168.239.133
REMOTE_SSH_HOST := $(REMOTEHOST_TWO)

# This specifies the location that will be used to import the image
REMOTE_SSH_HOST_LOCATION := $(REMOTEHOST_LOCATION_TWO)

# see if directory has git by calling the branch
ifeq ($(strip $(git branch --show-current 2> /dev/null)),)
	GIT_PRESENT := $(shell git branch --show-current 2> /dev/null)
endif

# if git is present, set build args
ifneq ($(strip $(GIT_PRESENT)),)
	VCS_REF := $(shell git rev-parse --short HEAD)
	BUILD_TIME_EPOCH := $(shell date +"%s")
	BUILD_TIME_RFC_3339 := $(shell date --rfc-3339='date')
	BUILD_TIME_UTC := $(shell date --utc)
	BUILD_ID := $(BUILD_TIME_UTC)-$(VCS_REF)
	GIT_STATUS := "is present"
else
	GIT_STATUS := "is not present"
endif

.PHONY: metadata

findjar:
	find build/ -iname '*.jar'

classpath:
	ag -G .java 'public static void main' src 

metadata:
	@echo Git is $(GIT_STATUS)
	@echo BUILD_TIME_EPOCH: $(BUILD_TIME_EPOCH)
	@echo BUILD_TIME_RFC_3339: $(BUILD_TIME_RFC_3339)
	@echo BUILD_TIME_UTC: $(BUILD_TIME_UTC)
	@echo BUILD_ID: $(BUILD_ID)

install:
	@echo "Installing dependencies..."
	docker pull $(IMAGES)

load:
	@echo "Exporting image from tar file..."
	docker load < $(APP).tar

save:
	@echo "Saving image into a tar file..."
	docker save $(JAVA_IMAGE):$(TAG) -o $(APP).tar

build: build/libs/$(JAR_FILE)
	docker build -t $(JAVA_IMAGE):$(TAG) --build-arg JAR_FILE=build/libs/$(JAR_FILE) .
	@echo "Removing dangling images"
	docker image prune -f

build-with-metadata: $(GIT_PRESENT)
	docker build -t $(JAVA_IMAGE):$(TAG) \
		--build-arg JAR_FILE=build/libs/$(JAR_FILE) \
		--build-arg BUILD_ID='$(BUILD_ID)' \
		--build-arg BUILD_DATE='$(BUILD_TIME_RFC_3339)' \
		--build-arg VCS_REF='$(VCS_REF)' \
		.
	@echo "$(JAVA_IMAGE):$(TAG) Image built using metadata. BUILD ID: $(BUILD_ID)"

build-debug:
	docker build -t $(JAVA_IMAGE)-debug:$(TAG) \
		--build-arg JAR_FILE=build/libs/$(JAR_FILE) \
		--target=debug-image .
	@echo "Debug image for the $(JAVA_IMAGE):$(TAG) built: $(JAVA_IMAGE)-debug:$(TAG)"

run:
	@echo "Running a testing container for the $(APP)..."
	docker run --rm --name $(APP) -p 8001:8001 --network perunanetti \
		--health-cmd='curl --silent --fail http://127.0.0.1:8001/actuator/health || exit 1' \
		--health-interval=5s \
		--health-timeout=10s \
		--health-retries=10 \
		--health-start-period=5s \
		$(JAVA_IMAGE):$(TAG)

run-debug:
	@echo "Running a debug container for the $(APP)..."
	docker run --rm --name $(APP)-debug -it $(JAVA_IMAGE)-debug:$(TAG)

runtime-debug:
	@echo "Running a debug container for final image..."
	docker run --rm --name $(APP)-debug-runtime -it --entrypoint sh $(JAVA_IMAGE):$(TAG)

run-detached:
	@echo "Running a testing container for the $(APP)..."
	docker run -d --rm --name $(APP) -p 8001:8001 --network perunanetti \
		--health-cmd='curl http://127.0.0.1:8001/actuator/health || exit 1' \
		--health-interval=15s \
		--health-timeout=10s \
		--health-retries=10 \
		--health-start-period=5s \
		$(JAVA_IMAGE):$(TAG)

deploy:
	docker stack deploy --compose-file $(APP).yml $(STACK)

deploy-with-time:
	docker stack deploy --compose-file $(APP).yml \
		$(APP)-$(shell date +"%Y%m%d_%H_%M")

deploy-local:
	docker-compose -f $(APP).yml up -d

.PHONY: inspect
inspect:
	docker image inspect --format '{{ json .Config.Labels }}' $(JAVA_IMAGE):$(TAG)

# NOTE: APP variable is named like the jar by convention, which is why it is used
# 			in the tar argument. If you do not follow similar convention, take note.
import:
	sh ../image-importer.sh \
		-h $(REMOTE_SSH_HOST) \
		-l $(REMOTE_SSH_HOST_LOCATION) \
		-t $(APP).tar \
		-i $(JAVA_IMAGE):$(TAG)

# lints the Dockerfile in the current directory
.PHONY: lint
lint:
	@echo "Linting Dockerfile"
	docker container run --rm -i hadolint/hadolint:v2.9.1-debian < Dockerfile

clean:
	docker rmi $(JAVA_IMAGE):$(TAG)
	docker rmi $(JAVA_IMAGE)-debug:$(TAG)

.PHONY: clean lint inspect runtime-debug run-debug run build-debug \
	build-with-metadata build save load install metadata
