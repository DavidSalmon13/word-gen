# Stage 1: Build the application with Maven and OpenJDK
FROM maven:3-openjdk-17 AS build

# Set the working directory for the build stage
WORKDIR /app

# Copy the Maven project files (pom.xml and source code)
COPY . .

# Run Maven to build the project (skip tests for faster builds)
RUN mvn clean package -DskipTests

# Stage 2: Run the application with Eclipse Temurin JRE (lighter runtime)
FROM eclipse-temurin:17-jre-alpine AS runtime

# Verify Java installation (you can skip this line if unnecessary)
RUN java -version

# Set the working directory for the runtime stage
WORKDIR /app

# Check where Java is located (optional for debugging)
RUN which java

# Copy the built JAR file from the build stage into the runtime stage
COPY --from=build /app/target/word-gen.jar app.jar

# Define the command to run the JAR file when the container starts
CMD ["java", "-jar", "app.jar"]

