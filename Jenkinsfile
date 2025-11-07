pipeline {
    agent any

    triggers {
        githubPush()   
    }

    stages {
        stage('Clone') {
            steps {
                git branch: 'develop', url: 'https://github.com/MouadBensafir/cargotracker.git'
            }
        }

        stage('Compile') {
            steps {
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
                bat 'mvn package'
            }
        }

        stage('SonarQube Analysis') {
            environment {
                SONAR_TOKEN = credentials('sonar-token-id')  // your Jenkins secret ID
            }
            steps {
                withSonarQubeEnv('SonarQube Local') {
                    bat 'mvn clean verify sonar:sonar -Dsonar.projectKey=cargo-tracker -Dsonar.projectName="Cargo Tracker" -Dsonar.host.url=http://localhost:9000 -Dsonar.token=sqp_2ce22addf05c4b51f8bbebb4b137833e83b89821'
                }
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
