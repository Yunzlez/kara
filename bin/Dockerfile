FROM openjdk:8-jdk-alpine

COPY target/kara-bin-1.2.5.jar /opt/kara.jar

EXPOSE 3400:3400

RUN sh -c "ls /opt"

ENTRYPOINT ["java", "-Xmx128M", "-Dspring.profiles.active=remote", "-jar", "/opt/kara.jar"]