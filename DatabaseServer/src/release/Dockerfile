# Database dockerfile
FROM jdk:21

WORKDIR /app/dumpster
COPY dumpster ./

WORKDIR /app
COPY DatabaseServer.jar ./
COPY entrypoint.sh ./

ENTRYPOINT ["/bin/sh"]
CMD ["entrypoint.sh"]
