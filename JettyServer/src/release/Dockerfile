# Jetty dockerfile
FROM jdk:17

WORKDIR /app

COPY JettyServer.jar ./
COPY entrypoint.sh ./
COPY dumpster.properties ./
COPY logging.properties ./

ENTRYPOINT ["/bin/sh"]
CMD ["entrypoint.sh"]
