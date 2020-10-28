pipeline {
  agent {
    kubernetes {
      yaml '''
apiVersion: v1
kind: Pod
spec:
  containers:
  - name: shell
    image: node:6-alpine
    command:
    - cat
    tty: true
'''
      defaultContainer 'shell'
    }

  }
  stages {
    stage('Build') {
      steps {
        sh '''pwd
ls -al
chmod +x gradlew
        '''
        sh './gradlew build --info'
      }
    }

    stage('Test') {
      steps {
        sh './gradlew test'
      }
    }

  }
}