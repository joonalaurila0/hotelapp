APP := elasticstack
STACK := hotelapp
TAG := 1.0

# ELK stack docker repository uris
ELASTICSEARCH := docker.elastic.co/elasticsearch/elasticsearch
LOGSTASH := docker.elastic.co/logstash/logstash
KIBANA := docker.elastic.co/kibana/kibana
# Shared version of the ELK stack
VERSION := 8.3.2-amd64

pull:
	docker pull $(ELASTICSEARCH):$(VERSION) \
		&& docker pull $(LOGSTASH):$(VERSION) \
		&& docker pull $(KIBANA):$(VERSION)

net:
	docker network create -d overlay --attachable perunanetti \
		--opt encrypted=true

deploy:
	docker stack deploy --compose-file elastic.yml $(STACK)

clean:
	docker stack rm $(STACK)
	docker rmi $(IMAGE_NAME):$(TAG)

inspect:
	@echo $(PWD)
	@echo $(HEAP_SIZE)

.PHONY: pull build run run-logstash run-zipkin clean net inspect
