

JAVA_VER=$(java -version 2>&1 | sed -n ';s/.* version "\(.*\)\.\(.*\)\..*".*/\1\2/p;')
if [[ "$JAVA_VER" -eq 18 ]]
then
  echo Java ver: $JAVA_VER, Java 8
  JDKARGS+=" -XX:+UnlockCommercialFeatures -XX:FlightRecorderOptions=loglevel=trace"
else
  echo Java ver: $JAVA_VER
  JDKARGS+=" --enable-preview -Xlog:jfr=info"
fi

JFRDUMPFILE="dumpster_diagnosis_$(date +"%Y_%m_%d_%I_%M_%p").jfr"
JMXARGS="-Dcom.sun.management.jmxremote.port=12346 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Djava.rmi.server.hostname=127.0.0.1"
echo Using java args: $JDKARGS

java $JDKARGS -Djava.util.logging.config.file=logging.properties $JMXARGS -XX:StartFlightRecording=filename=$JFRDUMPFILE,dumponexit=true -cp CliClient/target/CliClient-1.0-jar-with-dependencies.jar inside.dumpster.client.Cli $@


echo JFR file dumped to: $JFRDUMPFILE
