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

Log "INFO" "Cloning lastest version of sys__k8s_helm_chart.git..."

# Cloning latest version of helm charts.
git clone git@bitbucket.org:premfina/sys__k8s_helm_charts.git

# Must be set if we want to use Helm CLI commands.
export HELM_EXPERIMENTAL_OCI=1

Log "INFO" "Authenticating helm with Azure registry..."

# Authenticating helm with our Azure registry.
helm registry login $AZURE_REGISTRY -u $AZURE_REGISTRY_USERNAME -p $AZURE_REGISTRY_PASSWORD

# Remember v1 folder location.
cur=$(pwd)

# Using Makefile located in sys__k8s_helm_charts/v1.
cd sys__k8s_helm_charts/v1

Log "INFO" "Pulling Helm chart..."

# Save and push helm chart to repository
make pull APP=backend/be__affordability VERSION=$tag

Log "INFO" "Exporting Helm chart to project..."

# Placing the base helm chart to the right location.
make export APP=backend/be__affordability VERSION=$tag DIR=$cur/deploy/k8s

# Moving to location in order to deploy app.
cd $cur/deploy/k8s

# Loging in Azure.
AzureLogin $AZURE_PRINCIPAL_ID $AZURE_PRINCIPAL_PASSWORD $AZURE_TENANT_NAME $AZURE_SUBSCRIPTION_ID

# Getting credentials.
AzureGetCredentials $AZURE_RESOURCE_GROUP $AZURE_SERVICE_NAME

# Set kubectl context.
kubectl config use-context $KUBE_CONTEXT

# Installing app in the desired cluster. (variables below are defined in bitbucket > be__affordability > Repository Settings > Deployments)
helm upgrade -install $NAME ./be__affordability -n $NAMESPACE \
-f values.yaml \
--set replicasCount=$REPLICA \
--set image.tag=$tag \
--set configMaps.ingress.host=$HOST

# Cheking status of the deployment.
CheckStatus $NAMESPACE be-affordability

# Logging out of Azure.
AzureLogout