pipeline {
  agent {
    node {
      label 'jenkins-jenkins-slave'
    }

  }
  stages {
    stage('Build') {
      steps {
        sh '''pwd
ls -al'''
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