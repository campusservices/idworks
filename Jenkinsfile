pipeline {
  agent any
  stages {
   stage('Compile') {
      sh './mvnw -B -DskipTests clean package'
   } 
    stage('Deploy') {
        steps { 
          echo 'echo Deploy to Container'

           script  {
              def containerName = "idWorksContainer"
              
              def containerExists = sh(script: "docker ps -q -f name=${containerName}", returnStdout: true) == 0

              if (containerExists) then {
                      sh 'echo cleanup'
                      sh 'docker stop idWorksContainer'
                      sh 'docker rm idWorksContainer'
              } else {
               sh 'echo container does not exist'
              
              }
            }
        }
    }
	stage('Build') {
	    sh "pwd"
        sh 'docker-compose down'
        sh 'docker-compose up --detach --build'  
    }
  }
}