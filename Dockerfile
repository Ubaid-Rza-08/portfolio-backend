FROM maven:3.9.5-eclipse-temurin-17 AS build
WORKDIR /home/app

COPY . /home/app

RUN mvn -f /home/app/pom.xml clean package

FROM eclipse-temurin:17-jre
EXPOSE 8080

COPY --from=build /home/app/target/*.jar app.jar

ENTRYPOINT ["sh", "-c", "java -jar /app.jar"]

#docker build -t backend:0.0.2-SNAPSHOT .
# docker container run -d -p 5000:8080 backend:0.0.2-SNAPSHOT


#spring-boot:build-image
#docker container run -d -p 5000:8080 backend:0.0.1-SNAPSHOT