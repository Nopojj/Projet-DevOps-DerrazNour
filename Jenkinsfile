pipeline {
    agent any

    tools {
        maven 'Maven3'
    }

    triggers {
        pollSCM('H/2 * * * *')   // surveille le depot GitHub
    }

    environment {
        JAR = 'target/pipeline-demo-1.0-SNAPSHOT.jar'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'mvn -B clean compile'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn -B test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Run') {
            steps {
                sh 'mvn -B package -DskipTests'
                sh "java -jar ${JAR}"
            }
        }

        stage('Archive') {
            steps {
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }

        stage('Docker Build') {
            steps {
                sh "docker build -t pipeline-demo:${BUILD_NUMBER} -t pipeline-demo:latest ."
            }
        }

        stage('Deploy') {
            steps {
                sh '''
                mkdir -p deploy
                cp target/*.jar deploy/
                docker compose down || true
                docker compose up --abort-on-container-exit
                '''
            }
        }

        stage('Notify Slack') {
            steps {
                withCredentials([string(credentialsId: 'slack-webhook', variable: 'SLACK_WEBHOOK')]) {
                    sh """
                    curl -s -X POST -H 'Content-type: application/json' \\
                    --data '{"text":"SUCCES ${env.JOB_NAME} #${env.BUILD_NUMBER}"}' \\
                    "\$SLACK_WEBHOOK"
                    """
                }
            }
        }
    }

    post {
        failure {
            withCredentials([string(credentialsId: 'slack-webhook', variable: 'SLACK_WEBHOOK')]) {
                sh """
                curl -s -X POST -H 'Content-type: application/json' \\
                --data '{"text":"ECHEC ${env.JOB_NAME} #${env.BUILD_NUMBER}"}' \\
                "\$SLACK_WEBHOOK"
                """
            }
        }
        always {
            sh 'docker compose down || true'
        }
    }
}