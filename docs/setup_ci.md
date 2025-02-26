## Setup CI

## Roadmap

- [x] Instalação dos Plugins necessários

```yaml
additionalPlugins:
  - basic-branch-build-strategies:81.v05e333931c7d
  - multibranch-scan-webhook-trigger:1.0.9
  - pipeline-stage-view:2.34
  - discord-notifier:241.v448b_ccd0c0d6
```

- [x] Teste com uma sample pipeline
- [x] Jenkinsfile remoto
- [x] Configuração webhook Gitea <> Jenkins
- [x] Subir a aplicação localmente
- [x] Subir manualmente no Kubernetes
- [x] Entender os funciomaneto dos testes
- [x] Criar um stage para os testes
- [x] Testar o stage de `teste` na pipeline

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
- [x] Limitando as branches com `when`

```groovy
when {
  anyOf {
      branch pattern: "feature-*"
      branch pattern: "develop"
      branch pattern: "hotfix-*"
      branch pattern: "release-*"
      branch pattern: "v*"
  }
}
```
- [x] Mapeamento das branches da Multibranch Pipeline no Jenkins - Filter by name (with regular expression)

- Regular expression

```bash
^(develop|release|hotfix|v).*
```

<p align="center">
  <img alt="CI/CD" src="../images/filter-branch-jenkins.png">
</p>

## Harbor Security Scan

- Chamada da `api`
```bash
curl -X 'GET' \
'http://harbor.localhost.com/api/v2.0/projects/viana/repositories/restapi-flask-demo/artifacts/dev-2b26b9d3c8?page=1&page_size=10&with_tag=true&with_label=false&with_scan_overview=true&with_sbom_overview=false&with_accessory=false&with_signature=false&with_immutable_status=false' \
-H 'accept: application/json' \
-H 'X-Accept-Vulnerabilities: application/vnd.security.vulnerability.report; version=1.1, application/vnd.scanner.adapter.vuln.report.harbor+json; version=1.0' \
-H 'authorization: Basic YWRtaW46SGFyYm9yMTIzNDU=' | jq -r '.scan_overview | to_entries | .[].value.severity'
```

- Retry Backoff

```bash
#!/bin/bash

 # retry backoff parameters
MAX_RETRY=10
COUNT=1
SLEEP=1
SEVERITY="null"
# extract tag name
TAG=""
 
if [ $(echo $GIT_BRANCH | grep ^develop$) ]; then
    TAG="dev-${GIT_COMMIT:0:10}"
elif [ $(echo $GIT_BRANCH | grep -E "^(release-.*)|(hotfix-.*)") ]; then
    TAG="${GIT_BRANCH#*-}-${GIT_COMMIT:0:10}"
elif [ $(echo $GIT_BRANCH | grep -E "v[0-9]\\.[0-9]{1,2}\\.[0-9]{1,3}$") ]; then
    TAG="${GIT_BRANCH}"
fi
 
# harbor variables
HARBOR_URL="http://harbor.localhost.com"
HARBOR_PATH="api/v2.0/projects/viana/repositories/${JOB_NAME%/*}/artifacts/${TAG}"
HARBOR_URL_PARAMS="with_scan_overview=true"
 
while [ "$SEVERITY" == "null" ]; do
    SEVERITY=$(curl -X GET 'http://harbor.localhost.com/api/v2.0/projects/viana/repositories/restapi-flask-demo/artifacts/dev-d2dc6c49b7?page=1&page_size=10&with_tag=true&with_label=false&with_scan_overview=true&with_sbom_overview=false&with_accessory=false&with_signature=false&with_immutable_status=false' \
        -H 'accept: application/json' \
        -H 'X-Accept-Vulnerabilities: application/vnd.security.vulnerability.report; version=1.1, application/vnd.scanner.adapter.vuln.report.harbor+json; version=1.0' \
        -H 'authorization: Basic YWRtaW46SGFyYm9yMTIzNDU=' | jq -r '.scan_overview | to_entries | .[].value.severity')
 
    echo "sleep: ${SLEEP}s | count: ${COUNT}"
    sleep $SLEEP
    SLEEP=$(($SLEEP*2))
    COUNT=$(($COUNT+1))
 
    if [ $COUNT -ge $MAX_RETRY ]; then
        echo "Reached maximum retry of ${MAX_RETRY}, exiting..."
        exit 1
    fi
done
 
if [ "$SEVERITY" == "Critical" ]; then
    echo "There is Critical severity, please check on Harbor for the report"
    exit 1
else
    echo "All good, proceeding to the next stage"
fi
```


## Referências

- [Jenkins Plugins](https://plugins.jenkins.io/)