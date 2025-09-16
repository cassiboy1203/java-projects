FROM eclipse-temurin:21-alpine
RUN mkdir /server
WORKDIR /server
COPY ./server .
RUN chmod 500 ./start.sh

ENTRYPOINT ["./start.sh"]