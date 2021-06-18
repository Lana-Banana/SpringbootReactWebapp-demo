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
                    '''
        }
      }
    }

    stage('Gradle Build & Test') {
      steps {
        container(name: 'openjdk11') {
          sh '''
                    chmod +x gradlew
                    ./gradlew build --stacktrace
                    ./gradlew test
                    pwd
                    ls -al build/
                    '''
          stash(name: 'buildoutput', includes: 'build/**/*')
        }
      }
    }

    stage('Docker Image build & Push') {
      agent {
        kubernetes {
          yaml '''
apiVersion: v1
kind: Pod
spec:
  securityContext:
    runAsUser: 0
  containers:
  - name: kaniko
    image: gcr.io/kaniko-project/executor:debug
    command:
    - /busybox/cat
    tty: true
    volumeMounts:
    - name: docker-registry-config
      mountPath: /kaniko/.docker/
  volumes:
  - name: docker-registry-config
    secret:
      secretName: docker-registry-config
'''
        }
      }
      steps {
        container(name: 'kaniko') {
          unstash 'buildoutput'
	  withVault([
		    configuration: [vaultUrl: 'https://vault.srep-atomy.com',  vaultCredentialId: 'approle-for-vault', engineVersion: 2],
		    vaultSecrets: [[path: 'jenkins/harbor-bot-account', secretValues: [
			[envVar: 'CI_REGISTRY_USER', vaultKey: 'username'],
			[envVar: 'CI_REGISTRY_PASSWORD', vaultKey: 'password']
		]]]
		]) {
		  sh '''
			echo ${CI_REGISTRY_USER}
			echo ${CI_REGISTRY_PASSWORD}
			pwd
			ls -al
			ls -al /kaniko/.docker
			/kaniko/executor --context `pwd` --destination harbor.srep-atomy.com/emarket/spring-test
			'''
		 }
        }
      }
    }
  }
}
