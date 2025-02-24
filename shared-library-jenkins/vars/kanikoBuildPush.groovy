def call (body){

    def settings = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = settings
    body()

    container('kaniko'){
        sh '''
            IMAGE_TAG=""
            REGISTRY="harbor.localhost.com/viana"
            REPOSITORY=${JOB_NAME%/*}

            if [ $(echo $GIT_BRANCH | grep ^develop$) ]; then
                IMAGE_TAG="dev-${GIT_COMMIT:0:10}"
            elif [ $(echo $GIT_BRANCH | grep -E "^(release-.*)|(hotfix-.*)") ]; then
                IMAGE_TAG="${GIT_BRANCH#*-}-${GIT_COMMIT:0:10}"
            elif [ $(echo $GIT_BRANCH | grep -E "^v[0-9]\\.[0-9]\\.[0-9]$") ]; then
                IMAGE_TAG="${GIT_BRANCH}"
            fi
            
            DESTINATION=${REGISTRY}/${REPOSITORY}:${IMAGE_TAG}

            /kaniko/executor \
                --context $(pwd) \
                --destination ${DESTINATION} \
                --insecure
        '''
    }
}