# Use the official OpenJDK image as the base image
FROM eclipse-temurin:22

# Set the working directory
WORKDIR /app

# Copy the Spring Boot jar file to the container
COPY target/tiemcheit-be-0.0.1-SNAPSHOT.jar /app/tiemcheit.jar

# Expose the port the application runs on
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "tiemcheit.jar"]
