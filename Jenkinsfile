pipeline {
  agent {
    node {
      label 'jenkins-jenkins-slave'
    }

  }
  stages {
    stage('Build') {
      steps {
        tool(name: 'gradle', type: 'gradle6.7')
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