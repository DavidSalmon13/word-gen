# Use a base image with Maven and OpenJDK for building the app
FROM maven:3-openjdk-17 AS build

# Set the working directory for the build stage
WORKDIR /app

# Copy all files from the current directory to /app
COPY . .

# Run Maven to build the project (skip tests)
RUN mvn clean package -DskipTests

# Use a more explicit runtime image that includes Java (e.g., Eclipse Temurin)
FROM eclipse-temurin:17-jre-alpine AS runtime

# Verify Java installation (this is where you add the command)
RUN java -version

# Set the working directory for the runtime stage
WORKDIR /app

# Check where Java is located
RUN which java

# Copy the built JAR file from the build stage into the runtime image
COPY --from=build /app/target/word-gen.jar app.jar

# Define the default command to run the JAR file
CMD ["java", "-jar", "app.jar"]
