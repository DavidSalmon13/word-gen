# Use a base image with Java and Maven
FROM maven:3.8.6-openjdk-17 AS build

# Set work directory
WORKDIR /app

# Copy all files and build
COPY . .
RUN mvn clean package -DskipTests

# Use a smaller image just to run the app
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app
COPY --from=build /app/target/word-gen.jar app.jar

CMD ["java", "-jar", "app.jar"]
