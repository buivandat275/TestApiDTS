FROM maven:3.9.5-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Image để chạy ứng dụng
FROM eclipse-temurin:21-jre
WORKDIR /app
# Copy jar từ stage build
COPY --from=build /app/target/TestApiDTS-0.0.1-SNAPSHOT.jar app.jar

# Mở port (nếu cần)
EXPOSE 8080

# Lệnh chạy
ENTRYPOINT ["java", "-jar", "app.jar"]