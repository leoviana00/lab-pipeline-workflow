def call (body){

    def settings = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = settings
    body()

    pipeline {
        agent {
            kubernetes {
                yamlFile 'jenkinsPod.yaml'
            }
        }
        
        stages {
            stage('Unit Tests') {
                steps {
                    pythonUnitTest{}
                }
                when {
                    anyOf {
                        branch pattern: "feature-*"
                        branch pattern: "develop"
                        branch pattern: "hotfix-*"
                        branch pattern: "release-*"
                        branch pattern: "v*"
                    }
                }
            }

            stage('Quality Analysis') {
                environment {
                    SONAR_HOST_URL = "http://sonarqube.localhost.com"
                    SONAR_TOKEN    = credentials('sonar-scanner-cli') 
                }
                steps {
                    sonarqubeScan{}
                }
                when {
                    anyOf {
                        branch pattern: "feature-*"
                        branch pattern: "develop"
                        branch pattern: "hotfix-*"
                        branch pattern: "release-*"
                        branch pattern: "v*"
                    }
                }
            }

            stage('Build and Push') {

                steps {
                    kanikoBuildPush{}
                }
                when {
                    anyOf {
                        branch pattern: "develop"
                        branch pattern: "hotfix-*"
                    }
                }
            }

            stage('Harbor Security Scan') {
                environment {
                    HARBOR_CREDENTIALS = credentials('harbor-credentials')
                }
                steps {
                    harborSecurityScan{}
                }
                when {
                    anyOf {
                        branch pattern: "develop"
                        branch pattern: "hotfix-*"
                    }
                }
            }

            stage('Crane Artifact Promotion') {
                steps {
                    artifactPromotionCrane{}
                }
                when {
                    anyOf {
                        branch pattern: "release-*"
                        branch pattern: "v*"
                    }
                }
            }

            stage('Infrastructure Tests on K8s') {
                environment {
                    JENKINS_SSH_PRIVATE_KEY = credentials('giteaCredentials')
                }
                steps {
                    infraTestK8s{}
                }
                when {
                    anyOf {
                        branch pattern: "develop"
                        branch pattern: "hotfix-*"
                    }
                }
            }

            stage('Deploy to Development') {
                environment {
                    JENKINS_SSH_PRIVATE_KEY = credentials('giteaCredentials')
                }
                steps {
                    deployDev{}
                }
                when {
                    anyOf {
                        branch pattern: "develop"
                    }
                }
            }

            stage('Deploy to Staging') {
                environment {
                    JENKINS_SSH_PRIVATE_KEY = credentials('giteaCredentials')
                }
                steps {
                    deployStg{}
                }
                when {
                    anyOf {
                        branch pattern: "release-*"
                        branch pattern: "hotfix-*"
                    }
                }
            }

            stage('Create Tag?') {
                environment {
                    JENKINS_SSH_PRIVATE_KEY = credentials('giteaCredentials')
                }
                steps {
                    input message: "Would you like to promote to production?"
                    createTag {}
                }
                when {
                    anyOf {
                        branch pattern: "release-*"
                        branch pattern: "hotfix-*"
                    }
                }
            }

            stage('Deploy to Production') {
                environment {
                    JENKINS_SSH_PRIVATE_KEY = credentials('giteaCredentials')
                }
                steps {
                    input message: "Deploy to Production?"
                    deployPro {}
                }
                when {
                    anyOf {
                        branch pattern: "v*"
                    }
                }
            }
            
        }

        post {
            always {
                container('helm'){
                    sh '''
                        helm delete -n citest flask-ci
                    '''
                }
            }
        }
    }
}