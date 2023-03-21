set -v
#filename=D:/jsnordst/Bugs/InsideDumpster/dump.jfr,
#java -XX:StartFlightRecording=filename=D:/jsnordst/Bugs/InsideDumpster/,dumponexit=true -Dcom.sun.management.jmxremote.port=12345 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Djava.rmi.server.hostname=127.0.0.1 -jar MicronautServer/target/MicronautServer-1.0-SNAPSHOT.jar


java -XX:StartFlightRecording=filename=D:/jsnordst/Bugs/InsideDumpster/,dumponexit=true -Dcom.sun.management.jmxremote.port=12345 -Dcom.sun.management.jmxremote.authenticate=true -Dcom.sun.management.jmxremote.password.file=jmxremote-server.password -Dcom.sun.management.jmxremote.access.file=jmxremote.access -Dcom.sun.management.jmxremote.ssl=false -Djava.rmi.server.hostname=127.0.0.1 -jar MicronautServer/target/MicronautServer-1.0-SNAPSHOT.jar


#java -Dcom.sun.management.jmxremote.port=9999 
#-Dcom.sun.management.jmxremote.password.file=jmxremote.pa#ssword 
#-Djavax.net.ssl.keyStore=/home/user/.keystore 
#-Djavax.net.ssl.keyStorePassword=myKeyStorePassword 
#-Dcom.sun.management.jmxremote.ssl.need.client.auth=true 
#-Djavax.net.ssl.trustStore=/home/user/.truststore 
#-Djavax.net.ssl.trustStorePassword=myTrustStorePassword 
#-Dcom.sun.management.jmxremote.registry.ssl=true 
###-Djava.security.manager 
#-Djava.security.policy=jmx.policy 
#-jar lib/derbyrun.jar server start -h 0.0.0.0

set +v
