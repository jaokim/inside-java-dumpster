#!/bin/sh
# Runs the CLI client.
# Run with -h to get a list of options.
#

JAVA_VER=$(java -version 2>&1 | sed -n ';s/.* version "\(.*\)\.\(.*\)\..*".*/\1\2/p;')

#JDKARGS="-Djava.net.useSystemProxies=true"
# -Dhttp.proxyHost=${https_proxy} -Dhttp.proxyPort=80 "
#JDKARGS="-Djava.net.useSystemProxies=true -Dno_proxy -Dhttp.nonProxyHosts=\"localhost|host.example.com\""

if [[ "$JAVA_VER" -eq 18 ]]
then
  echo Java ver: $JAVA_VER, Java 8
  JDKARGS+=" -XX:+UnlockCommercialFeatures "
  JFRARGS="-XX:FlightRecorderOptions=loglevel=trace"
  JDKVERSION=8
else
  echo Java ver: $JAVA_VER
  JDKARGS+=" --enable-preview"
  JFRARGS="-Xlog:jfr=info"
  JDKVERSION=$JAVA_VER
fi

SERVER_PROC=JettyServer-1.0.jar

# jcmd $SERVER_PROC JFR.start name=rec maxsize=10M

JFRDUMPFILE="client_diagnosis_$VERSION_$(date +"%Y_%m_%d_%I_%M_%p").jfr"

JFRARGS+="-XX:StartFlightRecording=filename=$JFRDUMPFILE,dumponexit=true "
JMXARGS="-Dcom.sun.management.jmxremote.port=12346 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Djava.rmi.server.hostname=127.0.0.1"

Echo Using java args: $JDKARGS $JMXARGS $JFRARGS

java $JDKARGS $JMXARGS $JFRARGS -cp CliClient/target/CliClient-1.0-jar-with-dependencies.jar inside.dumpster.client.Cli $@

# jcmd $SERVER_PROC JFR.stop filename=$JFRDUMPFILE_SERVER name=rec


echo JFR file dumped to: $JFRDUMPFILE
