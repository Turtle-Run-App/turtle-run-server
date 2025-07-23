# 멀티스테이지 빌드 - 빌드 스테이지
FROM openjdk:21-jdk-slim AS builder

# 필요한 패키지 설치
RUN apt-get update && apt-get install -y \
    findutils \
    && rm -rf /var/lib/apt/lists/*

# 워크디렉토리 설정
WORKDIR /app
<<<<<<< HEAD
CMD ["./gradlew", "clean", "build"]
=======
>>>>>>> dev

# Gradle 래퍼와 빌드 설정 파일들을 먼저 복사
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Gradle 래퍼에 실행 권한 부여
RUN chmod +x ./gradlew

# 의존성 다운로드 (캐시 레이어 최적화)
RUN ./gradlew dependencies --no-daemon

# 소스 코드 복사
COPY src src

# 애플리케이션 빌드
RUN ./gradlew clean build --no-daemon

# 런타임 스테이지
FROM openjdk:21

# 워크디렉토리 설정
WORKDIR /app

# 빌드된 JAR 파일을 런타임 이미지로 복사
COPY --from=builder /app/build/libs/turtle-run-app-be.jar app.jar

# 애플리케이션 실행 포트 설정
EXPOSE 8081

# 애플리케이션 시작 명령어
CMD ["java", "-jar", "app.jar"]
