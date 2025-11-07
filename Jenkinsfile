pipeline {
    agent any

    stages {
        stage('Clone') {
            steps {
                git branch: 'develop', url: 'https://github.com/MouadBensafir/cargotracker.git'
            }
        }

        stage('Compile') {
            steps {
                // Use bat for Windows instead of sh
                mvn clean compile
            }
        }

        stage('Unit Tests') {
            steps {
                mvn test
            }
        }

        stage('Package') {
            steps {
                mvn package
            }
        }

        stage('SonarQube Analysis') {
            environment {
                SONAR_TOKEN = credentials('sonar-token-id')  // Replace with your Jenkins credential ID
            }
            steps {
                mvn sonar:sonar -Dsonar.projectKey=cargotracker -Dsonar.host.url="http://localhost:9000 -Dsonar.login=%SONAR_TOKEN%"
            }
        }
    }

    post {
        success {
            echo 'Build et analyse terminés avec succès !'
        }
        failure {
            echo 'Échec du build ou des tests.'
        }
    }
}
