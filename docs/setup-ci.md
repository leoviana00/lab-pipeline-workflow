# Pipeline

- Jenkinsfile

```yaml
@Library('jenkins-shared-libraries')_

pythonPipeline {}
```

## Shared Library Jenkins

- Pipeline Python

```groovy
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
                        branch pattern: "feature/**"
                        branch pattern: "develop"
                        branch pattern: "hotfix/**"
                        branch pattern: "release/**"
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
                        branch pattern: "feature/**"
                        branch pattern: "develop"
                        branch pattern: "hotfix/**"
                        branch pattern: "release/**"
                        branch pattern: "v*"
                    }
                }
            }
        }
    }
}
```

## Modulos a serem chamados na pipeline principal

- Stage `Unit Test`


```groovy
def call (body){

    def settings = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = settings
    body()

    container('python'){
        sh '''
            python3 -m venv .venv
            source ./.venv/bin/activate 
            pip install --upgrade pip
            pip install -r requirements.txt

            bandit -r . -x '/.venv/','/tests/'
            black .
            flake8 . --exclude .venv
            pytest -v --disable-warnings
        '''
    }
}
```

- Stage `Sonarqube Scan`

```groovy
def call (body){

    def settings = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = settings
    body()

    container('sonar-scanner-cli'){
        sh '''
           echo "JOB_NAME: ${JOB_NAME}" 

           sonar-scanner -X \
            -Dsonar.projectKey=${JOB_NAME%/*}-${GIT_BRANCH} \
            -Dsonar.qualitygate.wait=true
        '''
    }
}
```



## Container utilizados para execução dos stages

- Jenkins Pod `Unit Test`

```yaml
apiVersion: v1
kind: Pod
spec:
  containers:
  - name: python
    image: python:3.9.12-alpine3.15
    command:
    - sleep
    args:
    - infinity
    securityContext:
      # ubuntu runs as root by default, it is recommended or even mandatory in some environments (such as pod security admission "restricted") to run as a non-root user.
      runAsUser: 1000
```

- Jenkins Pod `Analise de código`

```yaml
  - name: sonar-scanner-cli
    image: sonarsource/sonar-scanner-cli:11.2
    command:
    - sleep
    args:
    - infinity
    securityContext:
      # ubuntu runs as root by default, it is recommended or even mandatory in some environments (such as pod security admission "restricted") to run as a non-root user.
      runAsUser: 1000
```