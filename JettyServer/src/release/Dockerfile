# Jetty dockerfile
FROM jdk:17

WORKDIR /app

COPY JettyServer.jar ./
COPY dumpster.properties ./
COPY logger.properties ./

ENTRYPOINT ["/bin/sh"]
CMD ["entrypoint.sh"]
