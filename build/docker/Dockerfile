FROM openjdk:17.0.2 as jre

WORKDIR /app

COPY target/affordability.jar .
COPY scripts/bash/entrypoint.sh .

CMD ["/bin/sh", "/app/entrypoint.sh"]
