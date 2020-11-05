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
  - name: openjdk11
    image: adoptopenjdk/openjdk11
    command:
    - cat
    tty: true
'''
            defaultContainer 'openjdk11'
        }
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
  - name: nodejs
    image: node:10.23-alpine
    command:
    - cat
    tty: true
'''
                }
            }
            steps {
                container(name: 'nodejs') {
                    sh '''
                    cd frontend
                    npm install
                    npm run build
                    '''
                    dir("frontend/build") {
                       stash "frontoutput"
                    }
                }
            }
        }
        stage('Gradle Build') {
            steps {
                sh '''
                mkdir frontend/build
                chmod +x gradlew
                '''
                dir("frontend/build") {
                   unstash "frontoutput"
                   sh "ls -al"
                }
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
