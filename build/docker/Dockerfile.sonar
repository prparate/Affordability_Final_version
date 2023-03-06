FROM sonarqube:9.5-community


USER root

RUN \

#ADD curl command
apk update && \ 
apk add curl && \

#Download jdk 17 and install
curl -o openjdk-17.0.2.tar.gz https://download.java.net/java/GA/jdk17.0.2/dfd4a8d0985749f896bed50d7138ee7f/8/GPL/openjdk-17.0.2_linux-x64_bin.tar.gz && \
tar -xvzf openjdk-17.0.2.tar.gz -C /usr/local && \ 
rm -rf openjdk-17.0.2.tar.gz 

ENV JAVA_HOME /usr/local/java-17-openjdk-17