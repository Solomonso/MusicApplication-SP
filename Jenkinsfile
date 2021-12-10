pipeline {
  agent any
  stages {
    stage('Development') {
      steps {
        withGradle()
      }
    }

    stage('testing') {
      steps {
        warnError(message: 'Something went wrong', catchInterruptions: true)
      }
    }

  }
}