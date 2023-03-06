#!/bin/bash

# Import functions defined in utils.sh
. ./scripts/bash/utils.sh --source-only

Log "INFO" "Configuring git..."

# Configure git to be able to pull internal modules.
git config --global --add url."git@bitbucket.org:".insteadOf "https://bitbucket.org/"

# Configure GOPRIVATE to accept premfina packages.
export GOPRIVATE=$GOPRIVATE

# Using pipeline SSH private key.
export SSH_PRIVATE_KEY="$(cat /opt/atlassian/pipelines/agent/ssh/id_rsa)"

Log "INFO" "Authenticating docker with Azure registry..."

# Authenticating docker with our Azure registry.
docker login $AZURE_REGISTRY -u $AZURE_REGISTRY_USERNAME -p $AZURE_REGISTRY_PASSWORD

# Extracting tag.
tag=$(echo $BITBUCKET_TAG | sed 's/v//') # strip the 'v' out

Log "INFO" "Building and pushing docker image..."

# Build and push docker image.
make build-push-ci TAG=$tag
