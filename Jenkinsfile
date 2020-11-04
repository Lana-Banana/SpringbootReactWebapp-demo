pipeline {
  agent {
    label 'jenkins-slave'
  }
  stages {
    stage('Npm Build') {
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
        }
      }
      steps {
        sh '''
        pwd
        ls -al
        cd frontend
        npm install
        npm run build
        '''
      }
    }
    stage('Gradle Build') {
      steps {
        sh '''
        cd ..
        pwd
        ls -al
        chmod +x gradlew
        '''
        sh './gradlew build --stacktrace'
      }
    }

    stage('Test') {
      steps {
        sh './gradlew test'
      }
    }

  }
}
