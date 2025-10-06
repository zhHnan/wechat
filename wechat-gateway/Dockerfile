FROM openjdk:17
EXPOSE 8098

MAINTAINER hnz_00@126.com
VOLUME /mydata
COPY /gateway.jar /gateway.jar
ENTRYPOINT ["java", "-jar","-Xms128m", "-Xmx256m", "/gateway.jar"]