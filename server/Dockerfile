FROM maven:3.9.9-eclipse-temurin-21 AS build

COPY di-framework /di-framework/

WORKDIR /di-framework
RUN mvn install

COPY yukikase-lib /yukikase-lib/

WORKDIR /yukikase-lib
RUN mvn install

COPY staffmanager-core /staffmanager-core/

WORKDIR /staffmanager-core
RUN mvn package

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
COPY --from=build /staffmanager-core/target/staffmanager-core*.jar plugins/

WORKDIR /server

ENTRYPOINT ["./start.sh"]