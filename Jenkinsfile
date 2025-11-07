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
                SONAR_TOKEN = credentials('sonar-token-id')  
            }
            steps {
                withSonarQubeEnv('SonarQube Local') {
                    bat "mvn clean verify sonar:sonar -Dsonar.projectKey=cargo-tracker -Dsonar.projectName=\"Cargo Tracker\" -Dsonar.host.url=http://localhost:9000 -Dsonar.token=${env.SONAR_TOKEN}"
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
