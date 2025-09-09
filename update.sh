#!/bin/bash

JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
PROJECT_DIR=~/playground
FRONTEND_BUILD_DIR=/var/www/playground
BACKEND_DIR="$PROJECT_DIR/back"
BACKEND_JAR_NAME="playground-backend.jar"
BACKEND_LOG="$PROJECT_DIR/back.log"

# 최신 코드 가져오기
cd "$PROJECT_DIR" || exit
echo "GIT PULLING..."
git pull origin main || { echo "Git Pull 실패"; exit 1; }

# 프론트엔드 빌드 & 복사
echo "BUILDING FRONTEND..."
cd "$PROJECT_DIR/front" || exit
npm install
npm run build || { echo "프론트엔드 빌드 실패"; exit 1; }
sudo cp -r dist/* "$FRONTEND_BUILD_DIR" || { echo "FAILED TO DEPLOY FRONTEND"; exit 1; }

# 백엔드 재시작
echo "BUILDING BACKEND..."
cd "$BACKEND_DIR" || exit
# Maven 빌드
./mvnw clean package -DskipTests || { echo "FAILED TO BUILD BACKEND"; exit 1; }
# jar 파일 찾기
JAR_PATH=$(find target -maxdepth 1 -type f -name "*.jar" | head -n 1)
if [ -z "$JAR_PATH" ]; then
  echo "JAR 파일을 찾을 수 없습니다."
  exit 1
fi
# 백엔드 실행 중이면 종료
PID=$(pgrep -f "$(basename "$JAR_PATH")")
if [ -n "$PID" ]; then
  echo "STOPPING EXISTING BACKEND PROCESS (PID: $PID)..."
  kill "$PID" || { echo "기존 백엔드 종료 실패"; exit 1; }
  sleep 3
fi
# 새 백엔드 실행
echo "STARTING BACKEND SERVER..."
chmod u+x "$JAR_PATH"
nohup $JAVA_HOME/bin/java -jar "$JAR_PATH" > "$BACKEND_LOG" 2>&1 &

echo "UPDATE DONE !"


# -----------------[ 메모 ]

# git clone {저장소주소}
# touch update.sh
# chmod 700 update.sh

# Nginx 기본설정파일: /etc/nginx/nginx.conf
# playground 프론트엔드 서버 설정: /etc/nginx/sites-available/playground


# python -m http.server 8080
# sudo netstat -tulnp | grep :8080
# sudo lsof -i :8080
# sudo kill <PID>




## express 서버 재시작
# echo "BUILDING BACKEND..."
# cd "$PROJECT_DIR/back" || exit
# npm install || { echo "FAILED TO npm-install"; exit 1; }
# echo "RESTARTING BACKEND SERVER..."
# pm2 restart playground-backend || { echo "FAILED TO START BACKEND"; exit 1; }

# pm2 start back/server.js --name playground-backend  # PM2로 백엔드 실행
# pm2 stop playground-backend
# pm2 restart playground-backend
# pm2 list  # PM2 실행 현황 보기
# tail -n1000 -f ~/.pm2/logs/playground-backend-out.log  # PM2 백엔드 로그 확인
# tail -n1000 -f ~/.pm2/logs/playground-backend-error.log  # PM2 백엔드 에러로그 확인
