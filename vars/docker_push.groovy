def call(String credId, String imageName, String versionTag){
  withCredentials([usernamePassword(
                    credentialsId:"${credId}",
                    passwordVariable: "dockerHubPass",
                    usernameVariable: "dockerHubUser"
                )]){
                
                sh "docker login -u ${env.dockerHubUser} -p ${env.dockerHubPass}"

                sh "docker image tag ${imageName} ${env.dockerHubUser}/${imageName}:latest"
                sh "docker image tag ${imageName} ${env.dockerHubUser}/${imageName}:${versionTag}"

                sh "docker push ${env.dockerHubUser}/${imageName}:latest"
                sh "docker push ${env.dockerHubUser}/${imageName}:${versionTag}"
            
                }  
}