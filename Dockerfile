# Use an official Amazon Corretto runtime as a parent image
FROM amazoncorretto:17-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the Gradle wrapper and build configuration files
COPY gradlew .
COPY gradle ./gradle
COPY build.gradle .
COPY settings.gradle .

# Copy the entire project
COPY . .

# Build the application with Gradle (omit tests if needed)
RUN ./gradlew build --no-daemon

# Expose the port your application runs on
EXPOSE 8080

# Run the jar file of your application
CMD ["java", "-jar", "build/libs/exchange.jar"]