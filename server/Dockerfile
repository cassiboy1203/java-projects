FROM maven:3.9.9-eclipse-temurin-21 AS build

COPY pom.xml .
COPY di-framework/ di-framework/
COPY yukikase-lib/ yukikase-lib/
COPY staffmanager/ staffmanager/

RUN mvn clean install

FROM eclipse-temurin:21-alpine

EXPOSE 25565 5005

RUN mkdir data
RUN mkdir server
WORKDIR /server
COPY server/eula.txt .
COPY server/purpur-1.21.4-2416.jar .
COPY --chmod=500 server/start.sh .

WORKDIR /data
COPY server/server.properties .
RUN mkdir plugins
COPY --from=build /yukikase-lib/target/yukikase-lib*.jar plugins/
COPY --from=build /staffmanager/core/target/staffmanager.core*.jar plugins/

WORKDIR /server

ENTRYPOINT ["./start.sh"]