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
chmod +x gradlew
        '''
        sh './gradlew build --debug'
      }
    }

    stage('Test') {
      steps {
        sh './gradlew test'
      }
    }

  }
}