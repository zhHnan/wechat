FROM openjdk:17
EXPOSE 8091

MAINTAINER hnz_00@126.com
VOLUME /mydata
COPY /file-service.jar /file-service.jar
ENTRYPOINT ["java", "-jar","-Xms128m", "-Xmx256m", "/file-service.jar"]