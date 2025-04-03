#!/bin/bash

# 작업 디렉토리 설정
PROJECT_DIR=~/playground
FRONTEND_BUILD_DIR=/var/www/playground
BACKEND_LOG=~/playground/back.log

# 최신 코드 가져오기
cd "$PROJECT_DIR" || exit
echo "Git Pulling..."
git pull origin main || { echo "Git Pull 실패"; exit 1; }

# 프론트엔드 빌드 & 복사
echo "building frontend..."
cd "$PROJECT_DIR/front" || exit
npm install
npm run build || { echo "프론트엔드 빌드 실패"; exit 1; }
sudo cp -r dist/* "$FRONTEND_BUILD_DIR" || { echo "FAILED TO DEPLOY FRONTEND"; exit 1; }

# 백엔드 재시작
echo "building backend..."
cd "$PROJECT_DIR/back" || exit
npm install || { echo "FAILED TO npm-install"; exit 1; }
echo "restarting backend server..."
pm2 restart playground-backend || { echo "FAILED TO START BACKEND"; exit 1; }

echo "UPDATE DONE !"


# -----------------[ 메모 ]

# git clone {저장소주소}
# touch update.sh
# chmod 700 update.sh

# Nginx 기본설정파일: /etc/nginx/nginx.conf
# playground 프론트엔드 서버 설정: /etc/nginx/sites-available/playground

# pm2 start back/server.js --name playground-backend  # PM2로 백엔드 실행
# pm2 stop playground-backend
# pm2 restart playground-backend
# pm2 list  # PM2 실행 현황 보기
# tail -n1000 -f ~/.pm2/logs/playground-backend-out.log  # PM2 백엔드 로그 확인
# tail -n1000 -f ~/.pm2/logs/playground-backend-error.log  # PM2 백엔드 에러로그 확인

# sudo netstat -tulnp | grep :8080
# sudo lsof -i :8080
# sudo kill <PID>
