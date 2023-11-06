FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/OtusAuth-1.0-SNAPSHOT.jar app.jar
EXPOSE 8010
ENTRYPOINT ["java","-jar","/app.jar"]