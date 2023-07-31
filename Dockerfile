FROM openjdk:17
ARG JAR_FILE=build/libs/app.jar
COPY ${JAR_FILE} ./app.jar
ADD src/main/resources/logback-prod.xml ./src/main/resources/logback-prod.xml
ENV TZ=Asia/Seoul
ENTRYPOINT ["java", "-jar", "./app.jar"]