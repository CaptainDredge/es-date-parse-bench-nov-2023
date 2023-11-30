mvn clean verify -DskipTests=true
java -agentlib:jdwp=transport=dt_socket,address=*:8080,server=y,suspend=n -jar target/benchmarks.jar
