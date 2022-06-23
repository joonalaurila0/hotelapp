APP := client
STACK := hotelapp
IMAGE_NAME := hotelapp/client
TAG := 1.0
TIME := 3

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
metadata:
	@echo Git is $(GIT_STATUS)
	@echo BUILD_TIME_EPOCH: $(BUILD_TIME_EPOCH)
	@echo BUILD_TIME_RFC_3339: $(BUILD_TIME_RFC_3339)
	@echo BUILD_TIME_UTC: $(BUILD_TIME_UTC)
	@echo BUILD_ID: $(BUILD_ID)

.PHONY: build
build:
	npm run clean && npm run build
	docker build -t $(IMAGE_NAME):$(TAG) .
	@echo "Removing dangling images"
	docker image prune -f

.PHONY: run
run:
	docker run --rm --name $(APP)-$(shell date "+%F") \
		-p 8081:8081 --network perunanetti \
		-d $(IMAGE_NAME):$(TAG)

deploy:
	docker stack deploy --compose-file $(APP).yml $(STACK)

.PHONY: clean
clean:
	docker rmi $(IMAGE_NAME):$(TAG)
