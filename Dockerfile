# Use litongjava/jdk:8u211 as the base image
FROM litongjava/jdk:8u391-stable-slim

# Set the working directory in the container
WORKDIR /app

# Copy the jar file into the container
COPY target/tio-boot-admin-1.0.jar /app/

# Command to run the jar file
CMD ["java", "-jar", "tio-boot-admin-1.0.jar"]