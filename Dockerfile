FROM amazoncorretto:25

RUN mkdir -p /opt/laa-spring-boot-ui/
WORKDIR /opt/laa-spring-boot-ui/

COPY build/libs/laa-spring-boot-ui-template-1.0.0.jar app.jar

USER 1001

EXPOSE 8082 8182

CMD java -jar app.jar
