#!/bin/bash

# SpringBoot 빌드
echo "Building SpringBoot Application..."
./gradlew clean build

# Docker 이미지 빌드
echo "Building Docker image..."
docker build -t hoho00/game-server .

# 도커 컴포즈 업 실행 (컨테이너 시작)
echo "Starting Docker containers with Docker Compose..."
docker-compose up -d

# 컨테이너 시작 후 추가 작업이 필요하다면 여기에 코드 작성

