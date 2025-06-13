# 사용하려는 베이스 이미지 지정
FROM openjdk:23-jdk

#CMD ["rm","-R", "./build/libs"]


# 워크디렉토리 설정
WORKDIR /app
CMD ["./gradlew", "clean", "build"]


# 애플리케이션 패키지 복사
COPY build/libs/turtle-run-app-be.jar app.jar

# 애플리케이션 실행 포트 설정
EXPOSE 8081

# 애플리케이션 시작 명령어
CMD ["java", "-jar", "app.jar"]
