# Build stage
FROM --platform=linux/amd64 maven:3.8-openjdk-8-slim AS build
WORKDIR /app

# 配置Maven使用阿里云镜像
RUN mkdir -p /root/.m2
# 直接创建settings.xml文件
RUN echo '<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 https://maven.apache.org/xsd/settings-1.0.0.xsd">\
  <mirrors>\
    <mirror>\
      <id>aliyunmaven</id>\
      <mirrorOf>*</mirrorOf>\
      <name>阿里云公共仓库</name>\
      <url>https://maven.aliyun.com/repository/public</url>\
    </mirror>\
  </mirrors>\
</settings>' > /root/.m2/settings.xml

# Copy pom.xml first to leverage Docker cache for dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code and build the application
COPY src ./src
RUN mvn package -DskipTests

# Runtime stage
FROM --platform=linux/amd64 openjdk:8-jre-slim
WORKDIR /app

# Create upload directory with proper permissions
#RUN mkdir -p /app/src/main/resources/static/upload && \
#    chmod 777 /app/src/main/resources/static/upload

# Copy the built JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# 复制静态资源文件夹中的内容到容器中
#COPY --from=build /app/src/main/resources/static/upload /app/src/main/resources/static/upload

# Set user to run the application (security best practice)
RUN useradd -m javauser
USER javauser

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"] 