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

# Extracting tag.
tag=$(echo $BITBUCKET_TAG | sed 's/v//') # strip the 'v' out

Log "INFO" "Cloning lastest version of lib__bitbucket_api.git..."

# Cloning latest version of bitbucket_api.
git clone git@bitbucket.org:premfina/lib__bitbucket_api.git

# Remember v1 folder location.
cur=$(pwd)

# Using Makefile located in lib__bitbucket_api.
cd lib__bitbucket_api

Log "INFO" "Compiling lib__bitbucket_api..."

# Compile the binary.
make compile

# Turning on Bitbucket API debug logs.
export DEBUG=true

# Specify path to the bitbucket configuration.
configPath=$cur/deploy/bitbucket/configuration.json

Log "INFO" "Setting deployment variables..."

# Running binary to update repository deployment variables.
# $1:   <string>  Comma-separated list of environments to update.
make run \
    CLIENT_ID=$BITBUCKET_CLIENT_ID \
    CLIENT_SECRET=$BITBUCKET_CLIENT_SECRET \
    WORKSPACE=$BITBUCKET_WORKSPACE \
    REPOSITORY=$BITBUCKET_REPO_SLUG \
    ENVIRONMENT=$1 \
    CONFIGPATH=$configPath