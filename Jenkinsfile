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
ls -al
        '''
        sh './gradlew assemble'
      }
    }

    stage('Test') {
      steps {
        sh './gradlew test'
      }
    }

  }
}