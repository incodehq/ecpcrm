node {
docker.withServer("tcp://${DOCKER_HOST}:${DOCKER_HOST_PORT}") {
stage('Create Network') {
    sh "docker network create 'docker-net-${BUILD_TAG}'"
  }
stage('Start docker slave container') {
    sh "docker run -d --net='docker-net-${BUILD_TAG}' --name 'docker-${BUILD_TAG}' --privileged -v docker-vol-${BUILD_TAG}:/var/lib/docker -e https_proxy='http://${HTTPS_PROXY_HOST}:${HTTPS_PROXY_PORT}' docker:1.12-dind"
  }
stage('Git clone repository') {
    sh "docker run --rm --name git -v git-vol-${BUILD_TAG}:/src -e https_proxy='http://$HTTPS_PROXY_HOST:$HTTPS_PROXY_PORT' incodehq/git sh -c 'git clone https://github.com/incodehq/ecpcrm.git /src/'"
  }
stage('Maven Build and Push') {
    sh "docker run --rm --net='docker-net-${BUILD_TAG}' --name maven-${BUILD_TAG} -i -v git-vol-${BUILD_TAG}:/src -v maven-home-global:/root/.m2/ -e MAVEN_OPTS='-DproxySet=true -DproxyHost=${HTTPS_PROXY_HOST} -DproxyPort=${HTTPS_PROXY_PORT}' -e DOCKER_HOST='tcp://docker-${BUILD_TAG}:2375' maven:3-alpine sh -c 'mvn clean package -Dmaven.test.skip=true -Dmavenmixin-docker -f /src && mvn -pl webapp deploy -Dmavenmixin-docker -f /src'"
  }
stage('Cleanup') {
	sh "docker rm -f docker-${BUILD_TAG} && docker volume rm docker-vol-${BUILD_TAG} git-vol-${BUILD_TAG} && docker network rm 'docker-net-${BUILD_TAG}'"
  }
}
}