#!/bin/bash

# Log will output the current datetime, the message and its level on the standard output.
# $1: level   <string> Level of the message. (INFO|OK|FAIL)
# $2: message <string> Message to output.
Log() {
    dt=$(date '+%Y-%m-%d %H:%M:%S');
    echo -e [$1] '\t' [$dt] $2
}

# Simply check if the port is reachable.
# $1: hostname     <string> Host we're trying to connect to.
# $2: port         <int>    Port we're trying to connect to.
# $3: service_name <string> Name of the service we're trying to connect to.
# $4: attempts     <int>    Number of attempts before exiting script.
# $5: sleep        <int>    Number of seconds to wait between two attempts.
# $6: debug        <bool>   Flag to output debug.
CheckPort() {
    local state=-1
    local attempt=0

    # Until port is accessible or maximum number of attemps has been reached.
    while [[ $attempt -lt $4 ]]
    do
        Log "INFO" "[$((attempt+1))/$4] Waiting for <$3> to start..."
        # Test if port is available.
        nc -z $1 $2
        state=$?
        if [[ $state -eq 0 ]]
        then
            break
        fi
        if [[ $6 -eq 1 ]]
        then
            docker ps
            docker logs affordability_$3
        fi
        Log "INFO" "Sleeping $5 seconds before next attempt to connect to <$3>..."
        sleep $5
        attempt=$((attempt+1))
    done

    if [[ $state -ne 0 ]]
    then
        Log "FAIL" "<$3> was not available on <$1:$2> after <$attempt> attempts."
        exit 1
    else
        Log "OK" "<$3> is listening on <$1:$2>."
    fi
}

# AzureLogin will simply login the Azure service principal.
# $1: id               <string> Azure service principal ID.
# $2: password         <string> Azure service principal password.
# $3: tenant           <string> Tenant name.
# $4: subscription-id  <string> Number of seconds to wait between two attempts.
AzureLogin() {
    Log "INFO" "Logging in Azure service principal..."

    az login --service-principal -u $1 -p $2 --tenant $3

    Log "INFO" "Setting Azure account subscription ..."

    az account set --subscription $4
}

# AzureGetCredentials will fetch the credentials for the Azure service principal.
# $1: resource-group <string> Resource group name.
# $2: service-name   <string> Service name.
AzureGetCredentials() {
    Log "INFO" "Getting Azure service principal credentials..."

    az aks get-credentials --resource-group $1 --name $2 --admin
}

# AzureLogout will simply proceed to log out the Azure service principal account.
AzureLogout() {
    Log "INFO" "Logging out Azure service principal..."

    az logout
}

# CheckStatus will try 15 times to check if the status is Ready, it will log a failure otherwise.
# $1: namespace  <string> Namespace in the cluster.
# $2: deployName <string> Name of the deployment.
CheckStatus() {
    local i=0
    local max=15

    while [[ $i -lt $max ]]
    do
        Log "INFO" "[$((i+1))/$max] Checking deployment status..."

        ready=$(kubectl -n $1 get pods -l app=$2 -o 'jsonpath={..status.conditions[?(@.type=="Ready")].status}' | awk '{print $1}' )

        if [[ $ready == "True" ]]
        then
            Log "OK" "New version deployed!"
            break
        fi

        i=$((i+1))

        Log "INFO" "Waiting 5 seconds..."

        sleep 5
    done

    if [[ $i -eq $max ]]
    then
        Log "FAIL" "New instance was not deployed! Check kibana for more information!"
        exit 1
    fi
}

#Escaping special characters in database credentials
EncodePipe() {
    local LANG=C
    local c
    while IFS= read -r c; do
        case $c in [a-zA-Z0-9.~_-])
            printf "$c"
            continue
            ;;
        esac
        printf "$c" | od -An -tx1 | tr ' ' % | tr -d '\n'
    done <<EOF
$(fold -w1)
EOF
    echo
}

Encode() { printf "$*" | EncodePipe; }
