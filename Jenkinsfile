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
  - name: nodejs
    image: node:10.23-alpine
    command:
    - cat
    tty: true
  - name: kaniko
    image: gcr.io/kaniko-project/executor:latest
    volumeMounts:
    - name: docker-config
      mountPath: /kaniko/.docker/
  volumes:
  - name: docker-config
    configMap:
      name: docker-config
'''
            defaultContainer 'jnlp'
        }
    }
    stages {
        stage('Npm Build') {
            steps {
                container(name: 'nodejs') {
                    sh '''
                    cd frontend
                    npm install
                    npm run build
                    ls -al
                    ls -al frontend/build
                    '''
                    //dir("frontend/build") {
                    //   stash "frontoutput"
                    //}
                }
            }
        }
        stage('Gradle Build') {
            steps {
                container(name: 'openjdk11') {
                    sh '''
                    // mkdir frontend/build
                    pwd
                    ls -al
                    ls -al frontend/build
                    chmod +x gradlew
                    '''
                    //dir("frontend/build") {
                    //   unstash "frontoutput"
                    //   sh "ls -al"
                    //}
                    sh './gradlew build --stacktrace'
                }
            }
        }

        stage('Test') {
            steps {
                sh './gradlew test'
            }
        }
        
        stage('Docker Image build') {
            steps {
                container(name: 'kaniko') {
                    sh 'docker build'
                }
            }
        }
    }
}
