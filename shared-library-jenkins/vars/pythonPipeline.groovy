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
                        branch pattern: "release-*"
                        branch pattern: "v*"
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
                        branch pattern: "release-*"
                        branch pattern: "v*"
                    }
                }
            }
            
        }
    }
}