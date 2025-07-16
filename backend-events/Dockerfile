# Stage 1: Build the application using Maven and Java 19
FROM maven:3.9-eclipse-temurin-19 AS build

# Set the working directory
WORKDIR /app

# Copy the pom.xml and download dependencies to leverage Docker cache
COPY pom.xml .
# Add a cache-busting argument
ARG CACHE_BUSTER=1

RUN mvn dependency:go-offline

# Copy the rest of the source code
COPY src ./src

# Package the application, skipping the tests
RUN mvn package

# Stage 2: Create the final, lightweight image with Java 19
FROM eclipse-temurin:19-jdk

# Set the working directory
WORKDIR /app

# Copy the executable JAR from the build stage
COPY --from=build /app/target/backend-events-0.0.1-SNAPSHOT.jar /app/backend-events.jar

# Expose the application port
EXPOSE 7700

# Specify the command to run on container startup
CMD ["java", "-jar", "/app/backend-events.jar"]
