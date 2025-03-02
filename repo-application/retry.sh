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
    SEVERITY=$(curl -X GET \
        'http://harbor.localhost.com/api/v2.0/projects/viana/repositories/restapi-flask-demo/artifacts/dev-24ef211ae7?page=1&page_size=10&with_tag=true&with_label=false&with_scan_overview=true&with_sbom_overview=false&with_accessory=false&with_signature=false&with_immutable_status=false' \
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