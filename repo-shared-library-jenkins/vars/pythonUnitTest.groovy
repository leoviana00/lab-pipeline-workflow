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