# Use a base image with Maven and OpenJDK for building the app
FROM maven:3-openjdk-17 AS build

# Set the working directory for the build stage
WORKDIR /app

# Copy all files from the current directory to /app
COPY . .

# Run Maven to build the project (skip tests)
RUN mvn clean package -DskipTests

# Use a smaller runtime image (eclipse-temurin with JRE)
FROM eclipse-temurin:17-jre-alpine AS runtime

# Verify Java installation (optional, for debugging)
RUN java -version

# Set the working directory for the runtime stage
WORKDIR /app

# Copy the built JAR file from the build stage into the runtime image
COPY --from=build /app/target/word-gen.jar app.jar

# Define the default command to run the JAR file
CMD ["java", "-jar", "app.jar"]

