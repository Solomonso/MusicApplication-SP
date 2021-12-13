pipeline {
  agent any
  stages {
   
    stage('Test') {
      steps {
        echo 'Compile project'
        buildPluginWithGradle()     
        }
    }

  }
  post {
        always {
            archiveArtifacts artifacts: 'build/libs/**/*.jar', fingerprint: true
            junit 'build/reports/**/*.xml'
        }
    }
}
