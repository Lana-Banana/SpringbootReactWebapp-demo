pipeline {
  agent {
        kubernetes {
            yaml '''
apiVersion: v1
kind: Pod
spec:
  securityContext:
    runAsUser: 1000
  containers:
  - name: jdk
    image: timbru31/java-node:11-jdk
    command:
    - cat
    tty: true
'''
            defaultContainer 'jdk'
        }
  }
  stages {
    stage('Npm Build') {
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
