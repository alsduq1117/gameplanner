#!/bin/bash

DOCKER_REPOSITORY=alsduq1117/jenkins
DOCKER_TAG=test

echo "> 현재 실행 중인 Docker Container ID 확인"
DOCKER_ID=$(docker ps -aqf "name=Jenkins")

echo "> 현재 실행 중인 Docker Container ID == $DOCKER_ID"
if [ -z "$DOCKER_ID" ]; then
  echo "> 현재 실행 중인 Docker Container가 존재하지 않습니다."
else
  echo "> 현재 실행 중인 컨테이너를 종료하겠습니다."
  docker stop $DOCKER_ID
  docker rm $DOCKER_ID
  sleep 10
fi

echo "> 실행 중인 Docker Container를 종료하였습니다."

echo "> Docker Image를 삭제하겠습니다."
docker rmi $DOCKER_REPOSITORY:$DOCKER_TAG

echo "> Docker Image를 가져옵니다."
docker pull $DOCKER_REPOSITORY:$DOCKER_TAG

echo "> Docker Image를 실행합니다."
docker run -d -p 8080:8080 --name Jenkins --env-file /home/ubuntu/chrkb1569/.env $DOCKER_REPOSITORY:$DOCKER_TAG