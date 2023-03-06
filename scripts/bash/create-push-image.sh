#!/bin/bash
export BITBUCKET_COMMIT_SHORT="${BITBUCKET_COMMIT::7}"
export TAG="$(date '+%Y.%m.%d')-${BITBUCKET_COMMIT_SHORT}"
export DOCKER_IMAGE_NAME="premfinaukcontainerregistry.azurecr.io/docker/backend/be__affordability"
export DOCKER_IMAGE="${DOCKER_IMAGE_NAME}:${TAG}"

docker login $AZURE_REGISTRY -u $AZURE_REGISTRY_USERNAME -p $AZURE_REGISTRY_PASSWORD

docker build -t $DOCKER_IMAGE .
docker push $DOCKER_IMAGE