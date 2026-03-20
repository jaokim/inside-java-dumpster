#!/bin/sh
# Runs the CLI client.
# Run with -h to get a list of options.
#

JAVA_VER=$(java -version 2>&1 | sed -n 's/.* version "\([0-9]*\).\([0-9]*\)[^"]*".*/\1\2/p;')

#JDKARGS="-Djava.net.useSystemProxies=true"
# -Dhttp.proxyHost=${https_proxy} -Dhttp.proxyPort=80 "
#JDKARGS="-Djava.net.useSystemProxies=true -Dno_proxy -Dhttp.nonProxyHosts=\"localhost|host.example.com\""


if [[ "$JAVA_VER" -eq 18 ]]
then
  echo Java ver: $JAVA_VER, Java 8
  JDKARGS+=" -XX:+UnlockCommercialFeatures"
  JFRARGS=" -XX:FlightRecorderOptions=loglevel=trace"
  JDKVERSION=8
else
  echo Java ver: $JAVA_VER
  JDKARGS+=" --enable-preview"
  #JFRARGS=" -Xlog=debug"
  JDKVERSION=$JAVA_VER
fi

JFRMETHODTRACE=
if [[ "$JAVA_VER" -eq 250 ]]
then
    JFRMETHODTRACE=",jdk.MethodTrace#filter=@inside.dumpster.bl.TraceMe,jdk.CPUTimeSample#enabled=true"
fi 

SERVER_PROC=JettyServer-1.0.jar
## @inside.dumpster.bl.TraceMe;


## ",jdk.MethodTrace#filter=inside.dumpster.bl.BusinessLogicServiceWrapper::invoke,jdk.MethodTiming#filter=inside.dumpster.energy.EnergyService::invoke"

##  inside.dumpster.bl.ServiceLookup::lookupServiceWrapper,
 ### ",jdk.MethodTrace#filter=inside.dumpster.energy.EnergyService::invoke,jdk.MethodTiming#filter=inside.dumpster.energy.EnergyService::invoke"

## ,inside.dumpster.uploadtext.UploadTextService::invoke,@inside.dumpster.bl.TraceMe"
# jcmd $SERVER_PROC JFR.start name=rec maxsize=10M

JFRDUMPFILE="client_diagnosis_JDK${JAVA_VER}_$(date +"%Y_%m_%d_%I_%M_%p").jfr"

JDKARGS+=" -Djava.util.logging.config.file=logging.properties "
JFRARGS+=" -XX:StartFlightRecording=filename=$JFRDUMPFILE,dumponexit=true${JFRMETHODTRACE} "
JMXARGS="-Dcom.sun.management.jmxremote.port=12346 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Djava.rmi.server.hostname=127.0.0.1"

Echo Using java args: $JDKARGS $JMXARGS $JFRARGS

java $JDKARGS $JMXARGS $JFRARGS -cp CliClient/target/CliClient-1.0-jar-with-dependencies.jar inside.dumpster.client.Cli $@

# jcmd $SERVER_PROC JFR.stop filename=$JFRDUMPFILE_SERVER name=rec


echo JFR file dumped to: $JFRDUMPFILE
jfr print --events jdk.MethodTrace,jdk.MethodTiming $JFRDUMPFILE