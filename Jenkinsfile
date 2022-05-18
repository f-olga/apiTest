pipeline {
    agent any

    stages {
        stage('Test') {
            steps {

                sh 'make check || true'
                sh './mvnw test'
            }
              post {
                always {
                  step([$class: 'Publisher', reportFilenamePattern: '**/testng-results.xml'])
                }
              }
        }
    }
}