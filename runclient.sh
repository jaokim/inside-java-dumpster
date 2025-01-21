

JAVA_VER=$(java -version 2>&1 | sed -n ';s/.* version "\(.*\)\.\(.*\)\..*".*/\1\2/p;')
#JDKARGS="-Djava.net.useSystemProxies=true"
# -Dhttp.proxyHost=${https_proxy} -Dhttp.proxyPort=80 "
#JDKARGS="-Djava.net.useSystemProxies=true -Dno_proxy -Dhttp.nonProxyHosts=\"localhost|host.example.com\""
if [[ "$JAVA_VER" -eq 18 ]]
then
  echo Java ver: $JAVA_VER, Java 8
  JDKARGS+=" -XX:+UnlockCommercialFeatures -XX:FlightRecorderOptions=loglevel=trace"
  JDKVERSION=8
else
  echo Java ver: $JAVA_VER
  JDKARGS+=" --enable-preview -Xlog:jfr=info"
  JDKVERSION=$JAVA_VER
fi

SERVER_PROC=JettyServer-1.0.jar
#SERVER_PROC=7272

# jcmd $SERVER_PROC JFR.start name=rec maxsize=10M


# This starts a 60 second JFR recording, after a 10 seconds warmup for a running Micronaut server
# Then it runs the webclient for 80 seconds
JFRDUMPFILE="client_diagnosis_$VERSION_$(date +"%Y_%m_%d_%I_%M_%p").jfr"

echo Using java args: $JDKARGS

java $JDKARGS -Dcom.sun.management.jmxremote.port=12346 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Djava.rmi.server.hostname=127.0.0.1 -XX:StartFlightRecording=filename=$JFRDUMPFILE,dumponexit=true -cp CliClient/target/CliClient-1.0-jar-with-dependencies.jar inside.dumpster.client.Cli $@

# jcmd $SERVER_PROC JFR.stop filename=$JFRDUMPFILE_SERVER name=rec


echo JFR file dumped to: $JFRDUMPFILE
