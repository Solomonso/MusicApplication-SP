pipeline {
  agent any
  stages {
   
    stage('Test') {
      steps {
        echo 'Compile project'
        echo 'Compile project'
        sh "chmod +x gradlew"
        sh './gradlew check'       
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
