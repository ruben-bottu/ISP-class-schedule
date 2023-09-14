pipeline {
    agent {
        kubernetes {
            yaml '''
apiVersion: v1
kind: Pod
metadata:
  labels:
    jenkins: agent
spec:
  containers:
  - name: alpine
    image: alpine:latest
    command:
      - cat
    tty: true
'''
        }
    }
    stages {
        stage('Echo Hello') {
            steps {
                container('alpine') {
                    sh 'echo hello'
                }
            }
        }
    }
}
