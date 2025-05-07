# Use a base image with Java and Maven
FROM maven:3.8.6-openjdk-17 AS build

# Set work directory
WORKDIR /app

# Copy all files and build
COPY . .
RUN mvn clean package -DskipTests

# Use a smaller image just to run the app
FROM eclipse-temurin:17-jre-alpine

# Install Java (optional, in case it's not already available)
# RUN apk update && apk add openjdk17-jre

# Verify Java installation (optional, for debugging)
RUN java -version

# Set working directory
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/target/word-gen.jar app.jar

# Run the JAR file
CMD ["java", "-jar", "app.jar"]

