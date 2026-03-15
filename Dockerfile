# ==========================================
# Stage 1: Build the Application
# ==========================================
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy the pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build the application (skipping tests speeds up deployment)
RUN mvn clean package -DskipTests

# ==========================================
# Stage 2: Run the Application
# ==========================================
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the built .jar file from the 'build' stage
# (Note: Render renames the matched jar to app.jar)
COPY --from=build /app/target/*.jar app.jar

# Expose the default web port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]