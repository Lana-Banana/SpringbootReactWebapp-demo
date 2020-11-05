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
                    //stash includes: "build/*", name: "frontoutput"
                }
            }
        }
        stage('Gradle Build') {
            steps {
                sh '''
                pwd
                ls -al
                ls -al frontend
                mkdir frontend/build
                ls -al frontend/build/
                chmod +x gradlew
                '''
                dir("frontend/build") {
                   //unstash "frontoutput"
                    sh "ls -al"
                }
                sh '''
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
