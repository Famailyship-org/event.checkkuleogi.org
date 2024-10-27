## 1. Base image 설정
#FROM openjdk:17
## 2. Working directory 생성
#WORKDIR /app
#
## 3. JAR 파일 복사
#ARG JAR_FILE=build/libs/fcfs-0.0.1-SNAPSHOT.jar
#COPY ${JAR_FILE} app.jar
#
## Prod 프로파일 활성화 설정
#ENV SPRING_PROFILES_ACTIVE=prod
#
## 4. Spring Boot 애플리케이션 실행 명령
#ENTRYPOINT ["java","-jar","/app.jar"]


FROM openjdk:17-jdk

COPY build/libs/fcfs-0.0.1-SNAPSHOT.jar app.jar

#ARG JAR_FILE=build/libs/fcfs-0.0.1-SNAPSHOT.jar
#
#COPY ${JAR_FILE} app.jar

#ENV TZ Asia/Seoul
#
#EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]

# ENTRYPOINT ["java","-Dfile.encoding=UTF-8", "-jar","/app.jar"]