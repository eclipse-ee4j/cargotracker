pipeline {
    agent any

    stages {
        stage('Clone') {
            steps {
                git branch: 'develop', url: 'https://github.com/MouadBensafir/cargotracker'
            }
        }

        stage('Compile') {
            steps {
                sh './mvnw clean compile'
            }
        }

        stage('Unit Tests') {
            steps {
                sh './mvnw test'
            }
        }

        stage('Package') {
            steps {
                sh './mvnw package'  // génère .war ou .jar dans target/
            }
        }

        stage('SonarQube Analysis') {
            environment {
                SONAR_TOKEN = credentials('sonar-token-id')
            }
            steps {
                sh "./mvnw sonar:sonar -Dsonar.projectKey=my-jee-project -Dsonar.host.url=http://localhost:9000 -Dsonar.login=$SONAR_TOKEN"
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
