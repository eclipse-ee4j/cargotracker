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
                bat 'mvn clean compile'
            }
        }

        stage('Unit Tests') {
            steps {
                bat 'mvn test'
            }
        }

        stage('Package') {
            steps {
                bat 'mvn package'  // Generates .jar or .war in target/
            }
        }

        stage('SonarQube Analysis') {
            environment {
                SONAR_TOKEN = credentials('sonar-token-id')  // Replace with your Jenkins credential ID
            }
            steps {
                bat "mvn sonar:sonar -Dsonar.projectKey=cargotracker -Dsonar.host.url=http://localhost:9000 -Dsonar.login=%SONAR_TOKEN%"
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
