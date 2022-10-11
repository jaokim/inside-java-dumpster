# This starts a 60 second JFR recording, after a 10 seconds warmup for a running Micronaut server
# Then it runs the webclient for 80 seconds
jcmd MicronautServer/target/MicronautServer-1.0-SNAPSHOT.jar JFR.start delay=10s duration=1m filename=diagnosis_$(date +"%Y_%m_%d_%I_%M_%p").jfr name=rec
java -jar WebClient/target/WebClient-1.0-SNAPSHOT-jar-with-dependencies.jar 80