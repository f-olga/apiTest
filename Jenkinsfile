pipeline {
    agent any

    stages {
        stage('Test') {
            steps {

                sh 'make check || true'
                sh 'mvn test'
            }
              post {
                always {
                  step([$class: 'Publisher', reportFilenamePattern: '**/testng-results.xml'])
                }
              }
        }
    }
}