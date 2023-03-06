docker_image_name = premfinaukcontainerregistry.azurecr.io/docker/backend/be__affordability
scripts_bash_dir = ./scripts/bash
dockerfile = build/docker/Dockerfile
deploy_dir = ./deploy
local_docker_compose = $(deploy_dir)/docker-compose/docker-compose.yml
sonar_docker_compose = $(deploy_dir)/docker-compose/docker-compose.sonar.yml

.PHONY: help
help:				## Displays the help.
	@fgrep -h "##" $(MAKEFILE_LIST) | fgrep -v fgrep | sed -e 's/\\$$//' | sed -e 's/##//'

.PHONY: start
start:				## Starts the dockerised app.
	@docker-compose --env-file .env.dist -f $(local_docker_compose) up -d
	@docker-compose --env-file .env.dist -f $(local_docker_compose) logs -f affordability_app

.PHONY: stop
stop:				## Stops the dockerised app.
	@docker-compose --env-file .env.dist -f $(local_docker_compose) down

.PHONY: start-infra
start-infra:		## Starts docker containers that power locally running app binary.
	@docker-compose --env-file .env.dist -f $(local_docker_compose) up -d postgres migrate
	@docker-compose --env-file .env.dist -f $(local_docker_compose) logs -f migrate

.PHONY: remove-all
remove-all:			## Stops docker containers and delete volumes and images.
	@docker-compose --env-file .env.dist -f $(local_docker_compose) down -v --rmi all

.PHONY: status
status:		        ## Status of services
	@docker-compose --env-file .env.dist -f $(local_docker_compose) ps

.PHONY: build
build:				## Builds the docker image. [requires TAG=${value}]
	docker build --no-cache -t $(docker_image_name):$(TAG) -f $(dockerfile) .

.PHONY: build-push-ci
build-push-ci:			## Builds and pushes the docker image for CI. [requires TAG=${value}]
	docker build --no-cache -t $(docker_image_name):$(TAG) -f $(dockerfile) .
	docker push $(docker_image_name):$(TAG)


.PHONY: generate-certificates
generate-certificates:		## Generates self signed certificates.
	@/bin/sh $(scripts_bash_dir)/certificate-generator.sh


.PHONY: sonar-start
sonar-start:		## starts sonarqube
	@docker-compose -f $(sonar_docker_compose) up -d