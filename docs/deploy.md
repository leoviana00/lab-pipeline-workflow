## Argo CD

- Install CLI

```bash
url -sSL -o argocd-linux-amd64 https://github.com/argoproj/argo-cd/releases/latest/download/argocd-linux-amd64   127 ↵ ──(dom,mar02)─┘
sudo install -m 555 argocd-linux-amd64 /usr/local/bin/argocd
```

- Login

```bash
argocd login argocd.localhost.com --insecure --username admin --password TDGY0AL6dy6Y52Oo
``` 

- Validar a autenticação

```bash
argocd account get-user-info
```

- Adicionar repositório

```bash
argocd repo add ssh://git@gitea-ssh.gitea.svc.cluster.local:2222/leoviana/helm-applications.git --ssh-private-key-path $HOME/.ssh/jenkins --insecure-skip-server-verification
```

- Checando qual pod possui determinado IP

```bash
kubectl get pod -A -owide | grep 10.244.2.15
```

- Checando o servicd gitea-ssh

```bash
kubectl describe -n gitea svc gitea-ssh 
```