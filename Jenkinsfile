pipeline {
  agent any
  stages {
    stage('Development') {
      steps {
        withGradle()
      }
      stage('Test') {
            steps {
                sh './gradlew check'
            }
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
