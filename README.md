Dreambox Learning exercise
Spelling REST service
=====================

This project depends on an installation of the following:
    for build, maven 3
        see maven.apache.org
    to run, java 8
        see oracle.com/technetwork/java/javase/downloads/index.html

To execute from the command line:
    java -jar target/spelling-0.0.1-SNAPSHOT.jar

To test from the command line:
  curl -i http://localhost:8080/spelling/$word

To rebuild and startup the web app with embedded tomcat:
    mvn clean spring-boot:run

To exit:
    ctrl-c

To create an executable jar:
    mvn clean package
    (creates spelling-0.0.1-SNAPSHOT.jar)



