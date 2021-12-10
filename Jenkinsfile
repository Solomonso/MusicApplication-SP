pipeline {
  agent any
  stages {
    stage('Development') {
      steps {
        withGradle()
      }
    }

  }
  post {
        always {
            junit 'build/reports/**/*.xml'
        }
    }
}
