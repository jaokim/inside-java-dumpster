# This starts a 60 second JFR recording, after a 10 seconds warmup for a running Micronaut server
# Then it runs the webclient for 80 seconds
JFRDUMPFILE="D:/jsnordst/Bugs/InsideDumpster/diagnosis_$(date +"%Y_%m_%d_%I_%M_%p").jfr"
#MicronautServer/target/MicronautServer-1.0-SNAPSHOT.jar
jcmd JettyServer/target/JettyServer-1.0-jar-with-dependencies.jar JFR.start name=rec

java -jar WebClient/target/WebClient-1.0-jar-with-dependencies.jar $@

jcmd JettyServer/target/JettyServer-1.0-jar-with-dependencies.jar JFR.stop filename=$JFRDUMPFILE name=rec

echo JFR file dumped to: $JFRDUMPFILE