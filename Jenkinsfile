pipeline {
    agent any
      tools {
        maven 'Maven-3'
    }
    environment {
        A = "Hello"
    }
    stages {
        stage('Build') {
            steps {
                bat 'mvn compile'
            }
        }
    stage('Static Analysis') {
                steps {
                    bat 'mvn checkstyle:check'
                }
            }
        stage('Test') {
            steps {
                bat 'mvn test'
            }
        }
        stage('Package') {
            steps {
                bat 'mvn package -DskipTests'
                echo A
            }
        }
    }
}