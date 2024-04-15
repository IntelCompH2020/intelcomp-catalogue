# Intelcomp Catalogue #

## Build

### Requirements
- Java 11
- Maven

### Build instructions
From the project root directory execute:
```bash
mvn clean package
```
The output is a "jar" file located under `target/*.jar`

## Deploy

### Dependencies
- Elasticsearch 7.x.x
- PostgreSQL 12 (a database with "tablefunc" extension enabled)
- ActiveMQ 5.x.x

### Deployment Instructions

1. Ensure that PostgreSQL, ActiveMQ and Elasticsearch are up and running.

2. Create a file named `application.yml` [Application Properties Example](#application-properties-example).

3. Run using Java
```bash
java -jar target/*.jar --spring.config.location=path/to/application.yml
```

- - -

## Application Properties Example
```yml
server:
  port: 8080
  servlet:
    contextPath: /api

spring:
  security:
    oauth2:
      client:
        registration:
          bsc:
            client-id: xxx
            client-secret: xxx
            scope:
              - openid
              - email
              - profile

        provider:
          bsc:
            issuer-uri: xxx

  redis:
    host: xxx
    port: xxx
    password: xxx

intelcomp:
  frontBaseUrl: http://localhost:4200
  loginRedirect: ${intelcomp.frontBaseUrl}
  logoutRedirect: ${intelcomp.frontBaseUrl}
  admins:
    - xxx@xxx.xxx

job-service:
  authorization:
    url: xxx
    grant-type: xxx
    client-id: xxx
    client-secret: xxx
```

![This project has received funding from the European Union’s Horizon 2020 research and innovation programme under grant agreement No. 101004870. H2020-SC6-GOVERNANCE-2018-2019-2020 / H2020-SC6-GOVERNANCE-2020](https://github.com/IntelCompH2020/.github/blob/main/profile/banner.png)
