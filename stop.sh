# 도커 컴포즈 다운 실행 (컨테이너 중지 및 리소스 정리)
echo "Stopping and removing Docker containers with Docker Compose..."
docker-compose down --remove-orphans

echo "Done!"