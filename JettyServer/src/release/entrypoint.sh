JAVA_VER=$(java -version 2>&1 | sed -n ';s/.* version "\(.*\)\.\(.*\)\..*".*/\1\2/p;')

if [[ "$JAVA_VER" -eq 18 ]]
then
  echo Java ver: $JAVA_VER, Java 8
  JDKARGS=-XX:+UnlockCommercialFeatures
else
  echo Java ver: $JAVA_VER
  JDKARGS=--enable-preview
fi

JDKARGS=$JDKARGS -XX:+PrintConcurrentLocks

JMXARGS=-Dcom.sun.management.jmxremote.port=12345 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Djava.rmi.server.hostname=127.0.0.1

JFRARGS= -XX:StartFlightRecording=filename=logs/dump.jfr,dumponexit=true

LOGARGS=-Djava.util.logging.config.file=logging.properties

#java $JDKARGS -XX:StartFlightRecording=filename=logs/dump.jfr,dumponexit=true -XX:+PrintConcurrentLocks -Dcom.sun.management.jmxremote.port=12345 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Djava.rmi.server.hostname=127.0.0.1 -Djava.util.logging.config.file=logging.properties -jar JettyServer/target/JettyServer-1.0-jar-with-dependencies.jar

java $JDKARGS $JFRARGS $JMXARGS $LOGARGS -jar JettyServer.jar