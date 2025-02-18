
# Setup Cluster
- Levantar e configurar um cluster kubernetes local


## Cluster com multinode

- Limites de arquivos a serem abertos pelo linux: Aumentar um pouco esse limite
- Persistência de parâmetros do kernel

```bash
echo fs.inotify.max_user_instances=1280 | sudo tee -a /etc/sysctl.conf 
echo fs.inotify.max_user_watches=655360 | sudo tee -a /etc/sysctl.conf 
sudo sysctl -p
```

- Criando o cluster
```bash
echo kind create cluster --name kind-workflow-pipe --config config.yaml  
```

## Filter docker

- Filtro por label
```bash
docker ps --filter="label=io.x-k8s.kind.role=worker" -q  
```

- Rodando comandos em todos os containers através de um for

```bash
for container in $(docker ps --filter="label=io.x-k8s.kind.role=worker" -q); do echo $container; done 
```

- Add DNS

```bash
for container in $(docker ps --filter="label=io.x-k8s.kind.role=worker" -q); do docker exec $container bash -c "echo '172.18.0.50 argocd.localhost.com jenkins.localhost.com gitea.localhost.com sonarqube.localhost.com harbor.localhost.com' >> /etc/hosts"; done 
```
- Utilizando um Daemonset para inserir DNS host
    - [Daemonset](./manifests/setup-hosts.yaml)

- Parando o cluster 
```bash
docker stop $(docker ps -q) 
```

- Startando o Cluster
```bash
docker start kind-workflow-pipe-control-plane kind-workflow-pipe-worker kind-workflow-pipe-worker2
```