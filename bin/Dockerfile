FROM openjdk:8-jdk-alpine

#ENV DATABASE_URI="mysql"
#ENV DATABASE_NAME="kara"
#ENV DATABASE_USR="kara"
#ENV DATABASE_PWD="kara"

COPY target/kara-bin-1.3.0.jar /opt/kara.jar

EXPOSE 3400:3400

ENTRYPOINT ["java", "-Xmx128M", "-Dspring.profiles.active=docker", "-jar", "/opt/kara.jar"]