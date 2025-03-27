# Database dockerfile
FROM jdk:21

WORKDIR /app/db
COPY dumpster ./

WORKDIR /app
COPY entrypoint.sh ./

ENTRYPOINT ["/bin/sh"]
CMD ["entrypoint.sh"]
