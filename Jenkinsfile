pipeline {
  agent any
  stages {
   stage('Compile') {
     steps { 
      sh './mvn clean install package'
      }
   } 
    stage('Deploy') {
        steps { 
          echo 'echo Deploy to Container'

           script  {
              def containerName = "idworkscontainer"
              
              def containerExists = sh(script: "docker ps -q -f name=${containerName}", returnStdout: true) == 0

              if (containerExists) then {
                      sh 'echo cleanup'
                      sh 'docker stop idworkscontainer'
                      sh 'docker rm idworkscontainer'
              } else {
               sh 'echo container does not exist'
              
              }
            }
        }
    }
	stage('Build') {
	    steps { 
		    sh "pwd"
	        sh 'docker-compose down'
	        sh 'docker-compose up --detach --build'  
        }
    }
  }
}