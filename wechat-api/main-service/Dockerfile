FROM openjdk:17
EXPOSE 8092

MAINTAINER hnz_00@126.com
VOLUME /mydata
COPY /main-service.jar /main-service.jar
ENTRYPOINT ["java", "-jar","-Xms128m", "-Xmx256m", "/main-service.jar"]