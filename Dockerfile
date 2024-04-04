### Build using Maven ###
FROM maven:3.8-jdk-11-slim AS maven

COPY pom.xml /tmp/
COPY . /tmp/

WORKDIR /tmp/

## run maven package ##
RUN mvn package -U -DskipTests


FROM openjdk:11

COPY --from=maven /tmp/target/*.jar /intelcomp-catalogue.jar
ENTRYPOINT ["java","-jar","/intelcomp-catalogue.jar"]

