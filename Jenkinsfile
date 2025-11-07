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

        // stage('SonarQube Analysis') {
        //     environment {
        //         SONAR_TOKEN = credentials('sonar-token-id')  // <-- replace with your Jenkins credential ID
        //     }
        //     steps {
        //         bat 'mvn sonar:sonar -Dsonar.projectKey=cargotracker -Dsonar.host.url=http://localhost:9000 -Dsonar.login=%SONAR_TOKEN%'
        //     }
        // } // info
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
