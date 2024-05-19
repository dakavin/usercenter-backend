# 使用基于 Alpine 的 Java 8 JDK 镜像
FROM openjdk:8-jdk-alpine

# 设置容器内的时区为上海
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 暴露 8080 端口，这是应用预设的运行端口
EXPOSE 8080

# 设置工作目录为 /app，之后的指令都将在这个目录下执行
WORKDIR /app

# 从构建上下文的 target 目录中复制 jar 文件到容器的工作目录
COPY target/usercenter-backend-0.0.1-SNAPSHOT.jar /app/App.jar

# 容器启动时运行 Java 应用
CMD ["java", "-jar", "/app/App.jar", "--spring.profiles.active=prod"]

# 测试两个库