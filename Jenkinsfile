pipeline {
    agent any

    stages {
        stage('Test') {
            steps {
                sh 'mvn clean test'
            }
              post {
                always {
                  step([$class: 'Publisher', reportFilenamePattern: '**/testng-results.xml'])
                }
              }
        }
    }
}