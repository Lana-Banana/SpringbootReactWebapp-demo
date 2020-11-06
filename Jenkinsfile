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
  - name: kubectl
    image: bitnami/kubectl
    command:
    - cat
    tty: true
    env:
    - name: "KUBECONFIG"
      value: "./kubeconfig"
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
                    stash name:'buildoutput', includes: 'build/**/*'
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
    - name: docker-config
      mountPath: /kaniko/.docker/
  volumes:
  - name: docker-config
    configMap:
      name: docker-config
'''
                }
            }
            steps {
                container(name: 'kaniko') {
                    unstash 'buildoutput'
                    sh '''
                    pwd
                    ls -al
                    '''
                    sh '/kaniko/executor --context `pwd` --destination 400603430485.dkr.ecr.ap-northeast-2.amazonaws.com/springbootwebapp:latest'
                }
            }
        }
        
        stage('Fetch Credential & Set kubeconfig') {
            steps {
                withVault([
                    configuration: [vaultUrl: 'https://dodt-vault.acldevsre.de',  vaultCredentialId: 'approle-for-vault', engineVersion: 2],
                    vaultSecrets: [[path: 'jenkins/dodt-dev-poc-oabe', secretValues: [[envVar: 'KUBECONFIG', vaultKey: 'kubeconfig']]]]
                ]){
                    sh '''
                    cat <<EOF > kubeconfig
                    ${KUBECONFIG}
                    '''
                    sh "sed -i -e '1,1s/^ *//' ./kubeconfig"
                }
            }
        }

        stage('Deploy Springboot Webapp') {
            steps {
                container('kubectl') {
                    sh '''
                    kubectl apply -f k8s/namespace.yaml
                    kubectl get ns
                    
                    kubectl apply -f k8s/deployment.yaml
                    kubectl get all -n springbootwebapp
                    
                    kubectl apply -f k8s/svc.yaml
                    kubectl get all -n springbootwebapp
                    '''
                }
            }
        }
    }
}
