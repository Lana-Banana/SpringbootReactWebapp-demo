// Uses Declarative syntax to run commands inside a container.
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
