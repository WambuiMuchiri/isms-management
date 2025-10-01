#Use OpenJDK base image
FROM eclipse-temurin:17-jdk

#Set Working Directory inside the container
WORKDIR /app

#Copy Built JAR file into the image
COPY target/isms_2025.jar app.jar

#Expose port Spring Boot runs on
EXPOSE 8085

#Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]


