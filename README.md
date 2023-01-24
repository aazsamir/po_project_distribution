# Compile
mvn install
mvn assembly:single

# Run
java -jar target/distribution-1.0-SNAPSHOT-jar-with-dependencies.jar

# Tests
mvn test