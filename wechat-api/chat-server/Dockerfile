FROM openjdk:17
EXPOSE 875

MAINTAINER hnz_00@126.com
VOLUME /mydata
# 复制Maven构建的jar包
COPY target/chat-server.jar /app.jar
# 设置工作目录
WORKDIR /
# 启动命令
ENTRYPOINT ["java", "-jar", "-Xms128m", "-Xmx256m", "/app.jar"]