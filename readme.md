
# BE__AFFORDABILITY


# Description
After all the following steps, will be possible to access the [open api specification](https://127.0.0.1:8080/swagger-ui/index.html)

## Prerequisites
- Git
- Java 17+
- Maven 3.8.4+
- Docker Compose 1.29.2+
- VPN connection


In order for this project to be executed, it is necessary to install a set of dependencies that it has and to configure the environment that will contain the necessary connections to the database.


### Connections Configuration

This project depends on connections to `Postgres` to run, so before running it, these services must first be available. For development, `docker-compose` is used to orchestrate a set of containers with the necessary services.

To run the services with `docker-compose` you first need to navigate to the folder where the `docker-compose.yml` file is (usually in the root of the project) and run the command below.

```bash
docker-compose --env-file .env.dist -f ./deploy/docker-compose/docker-compose.yml up -d postgres migrate
```
or
```bash
make start-infra
```

After executing the above command it is possible to see the status of the containers through the following command.

```bash
docker-compose --env-file .env.dist -f ./deploy/docker-compose/docker-compose.yml ps
```
or
```bash
make status
```
```text
         Name                       Command                  State               Ports
-----------------------------------------------------------------------------------------------------
affordability_migrate    migrate -path /database -d ...   Exit 0 
affordability_postgres   docker-entrypoint.sh postgres    Up (healthy)   0.0.0.0:5432->5432/tcp
```
If successful, postgres will be running and the application will be able to connect to it.
> :warning: Migrate container is down because It was executed and finished successfully.


### Logs
You can also see the logs of the containers through the following command.

```bash
docker-compose --env-file .env.dist -f ./deploy/docker-compose/docker-compose.yml logs -f
```

## Execute application

##### Execute dockerized application
```bash
make run
```
or
```bash
docker-compose --env-file .env.dist -f ./deploy/docker-compose/docker-compose.yml up -d
```

##### Execute application locally
```bash
 mvn clean package -DskipTests;
 java -jar target/affordability-0.0.1.jar
```# be__affordability
