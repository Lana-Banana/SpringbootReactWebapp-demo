pipeline {
  agent {
    node {
      label 'jenkins-jenkins-slave'
    }

  }
  stages {
    stage('Build') {
      tools {
        gradle 'gradle6.7'
      }
      steps {
        sh '''pwd
        ls -al
        '''
        sh 'gradle clean build'
      }
    }

    stage('Test') {
      steps {
        sh './gradlew test'
      }
    }

  }
}