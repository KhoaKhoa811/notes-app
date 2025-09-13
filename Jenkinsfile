pipeline {
    agent any

    environment {
        DOCKER_HUB = 'khoakhoa2004'                // DockerHub username
        IMAGE_BACKEND = 'notes-backend'
        IMAGE_FRONTEND = 'notes-frontend'
        VERSION_TAG = "build-${BUILD_NUMBER}"
        DEPLOY_DIR = '~/notes-app'
        PREV_VERSION = "build-${BUILD_NUMBER.toInteger() - 1}"  // rollback tag
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

        stage('Build & Test & SonarQube') {
            agent {
                docker {
                    image 'maven:3.9.6-eclipse-temurin-21'
                    args '-v /root/.m2:/root/.m2'
                }
            }
            steps {
                dir('backend') {
                    sh '''
                        # Build + run test + coverage
                        mvn clean test jacoco:report -Dspring.profiles.active=ci

                        # SonarQube analysis
                        mvn sonar:sonar \
                            -Dsonar.projectKey=notes_app \
                            -Dsonar.projectName=notes_app \
                            -Dsonar.host.url=$SONAR_HOST_URL \
                            -Dsonar.login=$SONAR_AUTH_TOKEN \
                            -Dsonar.junit.reportPaths=target/surefire-reports \
                            -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
                    '''
                }
            }
        }


        stage("Quality Gate") {
            steps {
                script {
                    timeout(time: 5, unit: 'MINUTES') {
                        def qg = waitForQualityGate()
                        if (qg.status != 'OK') {
                            error "Pipeline failed due to Quality Gate: ${qg.status}"
                        }
                    }
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
                script {
                    try {
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
                    } catch (err) {
                        echo "Deploy failed. Rolling back to previous version: ${PREV_VERSION}"
                        sshagent(['notes_app_ssh']) {
                            sh """
                                ssh -o StrictHostKeyChecking=no deploy@103.15.223.60 '
                                    cd $DEPLOY_DIR &&
                                    docker-compose -f docker-compose.yml -f docker-compose.prod.yml down &&
                                    docker pull $DOCKER_HUB/$IMAGE_BACKEND:${PREV_VERSION} &&
                                    docker pull $DOCKER_HUB/$IMAGE_FRONTEND:${PREV_VERSION} &&
                                    docker tag $DOCKER_HUB/$IMAGE_BACKEND:${PREV_VERSION} $DOCKER_HUB/$IMAGE_BACKEND:latest &&
                                    docker tag $DOCKER_HUB/$IMAGE_FRONTEND:${PREV_VERSION} $DOCKER_HUB/$IMAGE_FRONTEND:latest &&
                                    docker-compose -f docker-compose.yml -f docker-compose.prod.yml up -d
                                '
                            """
                        }
                        error("Rollback executed because Deploy failed.")
                    }
                }
            }
        }

    }
}
