FROM openjdk:17
EXPOSE 8080
ADD target/exhibitions.jar exhibitions.jar
ENTRYPOINT ["java", "-jar", "/exhibitions.jar"]