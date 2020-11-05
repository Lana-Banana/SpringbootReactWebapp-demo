pipeline {
    agent {
        kubernetes {
            yaml '''
apiVersion: v1
kind: Pod
spec:
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
                env
                pwd
                mkdir frontend/build
                ls -al frontend/build/
                '''
                dir("frontend/build") {
                   unstash "frontoutput"
                   sh "ls -al"
                }
                sh '''
                cd ../
                pwd
                ls -al
                ls -al frontend
                ls -al frontend/build/
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
