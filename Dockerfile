FROM openjdk:11
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} intelcomp-catalogue.jar
ENTRYPOINT ["java","-jar","/intelcomp-catalogue.jar"]

