def call(Map config) {
    def credentialsId = config.credentialsId
    def imageName     = config.imageName
    def buildTag      = config.buildTag

    withCredentials([usernamePassword(credentialsId: credentialsId, usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
        def fullImageName = "${env.DOCKER_USER}/${imageName}"
        
        sh """
            echo "Logging into DockerHub..."
            echo "\$DOCKER_PASS" | docker login -u "\$DOCKER_USER" --password-stdin

            echo "Pushing images..."
            docker push ${fullImageName}:${buildTag}
            docker push ${fullImageName}:latest

            echo "Logging out..."
            docker logout
        """
    }
}