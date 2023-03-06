#!/bin/bash

# Import functions defined in utils.sh
. ./scripts/bash/utils.sh --source-only

# CreateCertificateSigningRequest will create a new [C]ertificate [S]igning [R]equest.
# $1: days    <int>    Validity expressed in days.
# $2: key     <string> Private key filename.
# $3: csr     <string> CSR filename.
# $4: subject <string> Subject ot use.
# $5: isCA    <bool>   Is it a CA or not.
CreateCertificateSigningRequest() {
    if [[ $5 -eq 1 ]]
    then
        Log "INFO" "Generating a certificate signing request for the certificate authority..."

        openssl req -x509 -newkey rsa:4096 -days $1 -nodes -keyout $2 -out $3 -subj $4
    else
        Log "INFO" "Generating a certificate signing request for the server..."

        openssl req -newkey rsa:4096 -nodes -keyout $2 -out $3 -subj $4
    fi
}

# SignCertificateSigningRequest will sign the server [C]ertificate [S]igning [R]equest using the [C]ertificate [A]uthority.
# $1: serverCSR     <string> Server CSR filename.
# $2: days          <int>    Validity expressed in days.
# $3: caCertificate <string> CA certificate filename.
# $4: caPrivateKey  <string> CA private key filename.
# $5: serverCert    <string> Server certificate filename.
# $6: serverExt     <string> Server extension.
SignCertificateSigningRequest() {
    Log "INFO" "Signing server's certificate signing request..."

    openssl x509 -req -in $1 -days $2 -CA $3 -CAkey $4 -CAcreateserial -out $5 -extfile $6
}

folder="./certificates"
country="GB"
state="London"
location="London"
organisation="PremFina"
organisationalUnit="IT"
caCommonName="*.premfina.com"
serverCommonName="be-affordability.premfina.com"
email="tech@premfina.com"

# 1. Generate CA's private key and self-signed certificate

caSubject="/C=$country/ST=$state/L=$location/O=$organisation/OU=$organisationalUnit/CN=$caCommonName/emailAddress=$email"

CreateCertificateSigningRequest 365 $folder/ca-key.pem $folder/ca.crt $caSubject 1

# 2. Generate web server's private key and certificate signing request (CSR)

serverSubject="/C=$country/ST=$state/L=$location/O=$organisation/OU=$organisationalUnit/CN=$serverCommonName/emailAddress=$email"

CreateCertificateSigningRequest 365 $folder/server-key.pem $folder/server-req.pem $serverSubject 0

# 3. Use CA's private key to sign web server's CSR and get back the signed certificate

SignCertificateSigningRequest $folder/server-req.pem 60 $folder/ca.crt $folder/ca-key.pem $folder/server-cert.pem $folder/server-ext.cnf
