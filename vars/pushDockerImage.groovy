def call(Map config) {
    def credentialsId = config.credentialsId
    def registryUser = config.registryUser
    def imageName = config.imageName
    def buildTag = config.buildTag
    
    try {
        echo "📤 Pushing image to DockerHub..."
        
        withCredentials([usernamePassword(
            credentialsId: credentialsId,
            passwordVariable: 'DOCKER_PASS',
            usernameVariable: 'DOCKER_USER'
        )]) {
            sh """
                echo "🔐 Logging into DockerHub..."
                echo \${DOCKER_PASS} | docker login -u \${DOCKER_USER} --password-stdin
                
                echo "🏷️ Tagging image..."
                docker tag ${imageName}:${buildTag} ${registryUser}/${imageName}:${buildTag}
                docker tag ${imageName}:${buildTag} ${registryUser}/${imageName}:latest
                
                echo "📤 Pushing to DockerHub..."
                docker push ${registryUser}/${imageName}:${buildTag}
                docker push ${registryUser}/${imageName}:latest
                
                echo "🚪 Logging out from DockerHub..."
                docker logout
                
                echo "✅ Image pushed successfully"
            """
        }
    } catch (Exception e) {
        error "❌ Docker push failed: ${e.message}"
    }
}
