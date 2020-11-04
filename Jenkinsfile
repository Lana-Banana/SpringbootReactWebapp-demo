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
    stage('Build') {
      steps {
        sh '''pwd
ls -al
chmod +x gradlew
chown -R 1000:0 "/.npm"
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
