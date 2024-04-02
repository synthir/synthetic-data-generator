FROM openjdk:17
EXPOSE 8081
COPY src/main/resources src/main/resources
ARG JAR_FILE=target/synthetic-fhir-data-services-0.0.1.jar
COPY ${JAR_FILE} syntheticfhirdata.jar
ENTRYPOINT ["java","-jar","/syntheticfhirdata.jar"]