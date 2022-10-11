set -v
java -XX:StartFlightRecording=filename=dump.jfr,dumponexit=true -Dcom.sun.management.jmxremote.port=12345 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Djava.rmi.server.hostname=127.0.0.1 -jar MicronautServer/target/MicronautServer-1.0-SNAPSHOT.jar
set +v