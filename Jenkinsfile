pipeline {
  agent {
    node {
      label 'jenkins-slave'
    }

  }
  stages {
    stage('Build') {
      steps {
        withGradle() {
          sh 'sh \'./gradlew assemble\''
        }

      }
    }

    stage('Test') {
      steps {
        sh 'sh \'./gradlew test\''
      }
    }

  }
}