# ---- Stage 1: Build ----
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# copy pom.xml và tải dependency trước (tối ưu cache)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# copy code và build jar
COPY src ./src
RUN mvn clean package -DskipTests

# ---- Stage 2: Run ----
FROM eclipse-temurin:21-jdk
WORKDIR /app

# copy jar từ stage build
COPY --from=build /app/target/*.jar app.jar

# cổng mà Spring Boot expose (ví dụ 8080)
EXPOSE 8080

# lệnh chạy app
ENTRYPOINT ["java", "-jar", "app.jar"]
