def call(Map config) {
    def credentialsId = config.credentialsId
    def imageName      = config.imageName
    def buildTag       = config.buildTag

    // hard fail early if buildTag missing
    if (!buildTag || buildTag.toString().trim() == "") {
        error "pushDockerImage: buildTag is null/empty. Jenkinsfile must pass buildTag: IMAGE_TAG (e.g., BUILD_NUMBER)."
    }

    withCredentials([usernamePassword(
        credentialsId: credentialsId,
        usernameVariable: 'DOCKER_USER',
        passwordVariable: 'DOCKER_PASS'
    )]) {
        sh """
            set -e

            echo "🔐 Logging into DockerHub..."
            echo "\${DOCKER_PASS}" | docker login -u "\${DOCKER_USER}" --password-stdin

            echo "🏷️ Tagging image..."
            docker tag ${imageName}:${buildTag} \${DOCKER_USER}/${imageName}:${buildTag}
            docker tag ${imageName}:${buildTag} \${DOCKER_USER}/${imageName}:latest

            echo "📤 Pushing to DockerHub..."
            docker push \${DOCKER_USER}/${imageName}:${buildTag}
            docker push \${DOCKER_USER}/${imageName}:latest

            echo "🚪 Logging out from DockerHub..."
            docker logout

            echo "✅ Image pushed successfully"
        """
    }
}
