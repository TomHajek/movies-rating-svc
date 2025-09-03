FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY target/*.jar movie-rating-svc.jar
ENTRYPOINT ["java","-jar","movie-rating-svc.jar"]