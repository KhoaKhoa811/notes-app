# ---- Stage 1: Build ----
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# copy pom.xml và tải dependency trước (cache tốt hơn)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# copy code backend
COPY src ./src

# build jar (in ra target để kiểm tra)
RUN mvn clean package -DskipTests && ls -lh target

# ---- Stage 2: Run ----
FROM eclipse-temurin:21-jdk
WORKDIR /app

# copy jar từ stage build
COPY --from=build /app/target/*.jar app.jar

# Expose port Spring Boot
EXPOSE 8080

# Active profile thông qua biến môi trường
ENV SPRING_PROFILES_ACTIVE=default

# Command chạy app
ENTRYPOINT ["sh", "-c", "java -jar app.jar --spring.profiles.active=${SPRING_PROFILES_ACTIVE}"]
