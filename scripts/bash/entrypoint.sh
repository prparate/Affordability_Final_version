#!/bin/sh

# Importing self signed certificate in Java keystore
# -keystore and -cacerts - path to cacerts in which certificate details will be imported
# -storepass - password to keystore
# -noprompt - system will not ask to trust certificate
# -trustcacerts - to trust self sign certificate
# -importcert - will define certificate path and alias
# If certificates are not imported in java keystore will throw javax.net.ssl.SSLHandshakeException: unable to find valid certification path to requested target
keytool \
    -keystore -cacerts \
    -storepass changeit \
    -noprompt \
    -trustcacerts \
    -importcert \
    -file /app/cert/ca.crt \
    -alias selfsigned

# Starting the app
java \
-XX:MaxRAMPercentage=100 \
-XX:InitialRAMPercentage=25 \
-Djava.security.egd=file:/dev/./urandom \
-jar /app/affordability.jar