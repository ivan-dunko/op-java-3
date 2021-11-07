call mvn package
java -javaagent:agent\target\agent-1.0-SNAPSHOT.jar -jar java-perf\target\java-perf-1.0-SNAPSHOT.jar
