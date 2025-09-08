pipeline {
    agent any

    environment {
        DOCKER_HUB = 'khoakhoa2004'                // DockerHub username
        IMAGE_BACKEND = 'notes-backend'
        IMAGE_FRONTEND = 'notes-frontend'
        VERSION_TAG = "build-${BUILD_NUMBER}"
        DEPLOY_DIR = '~/notes-app'
    }

    triggers {
        githubPush()
    }

    stages {
        
        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/KhoaKhoa811/notes-app.git',
                    credentialsId: 'notes_app_git'
            }
        }

        stage('Build Docker Images') {
            steps {
                sh "docker build -t $DOCKER_HUB/$IMAGE_BACKEND:$VERSION_TAG -t $DOCKER_HUB/$IMAGE_BACKEND:latest ./backend"
                sh "docker build -t $DOCKER_HUB/$IMAGE_FRONTEND:$VERSION_TAG -t $DOCKER_HUB/$IMAGE_FRONTEND:latest ./frontend"
            }
        }

        stage('Push Docker Images') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'notes_app_dockerhub',
                    usernameVariable: 'USER',
                    passwordVariable: 'PASS'
                )]) {
                    sh "echo $PASS | docker login -u $USER --password-stdin"
                    sh "docker push $DOCKER_HUB/$IMAGE_BACKEND:$VERSION_TAG"
                    sh "docker push $DOCKER_HUB/$IMAGE_BACKEND:latest"
                    sh "docker push $DOCKER_HUB/$IMAGE_FRONTEND:$VERSION_TAG"
                    sh "docker push $DOCKER_HUB/$IMAGE_FRONTEND:latest"
                }
            }
        }

        stage('Deploy') {
            steps {
                sshagent(['notes_app_ssh']) {
                    sh """
                        ssh -o StrictHostKeyChecking=no deploy@103.15.223.60 '
                            cd $DEPLOY_DIR &&
                            docker-compose -f docker-compose.yml -f docker-compose.prod.yml down &&
                            git pull origin main &&
                            docker-compose -f docker-compose.yml -f docker-compose.prod.yml pull &&
                            docker-compose -f docker-compose.yml -f docker-compose.prod.yml up -d
                        '
                    """
                }
            }
        }
    }
}
