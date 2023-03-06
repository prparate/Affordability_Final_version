#!/bin/bash
mvn sonar:sonar \
  -Dsonar.login=$LOGIN_TOKEN \
  -Dsonar.host.url=$SONARQUBE_URL