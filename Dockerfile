# AS <NAME> to name this stage as maven
FROM maven:3.6.3 AS maven

WORKDIR /usr/src/app

COPY . /usr/src/app
# Compile and package the application to an executable JAR
RUN mvn clean install package 

# For Java 8, 
FROM adoptopenjdk/openjdk8:alpine-jre

ARG JAR_FILE

# Copy the idworks.jar from the maven stage to the /opt/app directory of the current stage.
COPY  --from=maven /usr/src/app/target/*.jar /opt/app/app.jar

WORKDIR /opt/app



EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]