FROM openjdk:17
EXPOSE 8090

MAINTAINER hnz_00@126.com
VOLUME /mydata
COPY /auth-service.jar /auth-service.jar
ENTRYPOINT ["java", "-jar","-Xms128m", "-Xmx256m", "/auth-service.jar"]