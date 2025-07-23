#!/bin/bash

# 개발용 스크립트 - DB만 컨테이너로 실행, Spring 애플리케이션은 로컬에서 실행

echo "Starting development environment..."

# 기존 컨테이너 정리
echo "Cleaning up existing containers..."
docker-compose down

# DB 컨테이너만 빌드 및 실행
echo "Building and starting database container..."
docker-compose up -d db

# DB가 완전히 시작될 때까지 대기
echo "Waiting for database to be ready..."
sleep 10

# DB 상태 확인
echo "Checking database status..."
docker-compose ps db

echo ""
echo "Development environment is ready!"
echo "Database is running in container on port 5432"
echo "You can now run your Spring application locally with:"
echo "  ./gradlew bootRun"
echo ""
echo "To stop the database container, run:"
echo "  docker-compose down" 