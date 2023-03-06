#!/bin/bash
set -e
git clone git@bitbucket.org:premfina/be__api_automation.git
cd be__api_automation
npm ci
mkdir cypress/config
echo $CONFIG_DEV
echo $CONFIG_DEV > cypress/config/development.json
npm run cy:run:apiTests:dev
npm run cy:run:reports