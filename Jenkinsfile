pipeline {
    agent any

    environment {
        DOCKER_HUB = 'khoakhoa2004'
        IMAGE_BACKEND = 'notes-backend'
        IMAGE_FRONTEND = 'notes-frontend'
        VERSION_TAG = "build-${env.BUILD_NUMBER}"
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', 
                    url: 'https://github.com/KhoaKhoa811/notes-app.git',
                    credentialsId: 'notes_app_git'
            }
        }

        stage('Build Backend') {
            steps {
                dir('backend') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Build Frontend') {
            steps {
                dir('frontend') {
                    sh 'npm install'
                    sh 'npm run build'
                }
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
                sh 'docker compose -f docker-compose.prod.yml up -d --build'
            }
        }
    }
}
