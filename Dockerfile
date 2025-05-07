# Use Maven with OpenJDK 17 as the build image
FROM maven:3.8.7-openjdk-17 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy all files into the container
COPY . .

# Make the Maven wrapper executable
RUN chmod +x mvnw

# Build the project using Maven wrapper
RUN ./mvnw clean package -DskipTests

# Stage 2: Run the built JAR
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=build /app/target/word-gen.jar app.jar

# Run the app
CMD ["java", "-jar", "app.jar"]