# Setup Cluster Tools

- Instalação de ferramentas no Cluster

## Metallb

- Identificar a rede utilizada pelo kind

```bash
docker network ls | grep kind
docker inspect <network>
docker inspect kind | jq -r '.[].IPAM.Config[0].Subnet'
```

- Instalação do Metallb

```bash
kubectl apply -f https://raw.githubusercontent.com/metallb/metallb/v0.14.3/config/manifests/metallb-native.yaml
kubectl wait --namespace metallb-system \
```

- Layer 2 Configuration

```yaml
apiVersion: metallb.io/v1beta1
kind: IPAddressPool
metadata:
  name: homelab-pool
  namespace: metallb-system
spec:
  addresses:
  - 172.18.0.50-172.18.0.70
---
apiVersion: metallb.io/v1beta1
kind: L2Advertisement
metadata:
  name: home-advertisement
  namespace: metallb-system
spec:
  ipAddressPools:
  - homelab-pool
```

- [Documentation - Inatallation by manifest](https://metallb.io/installation/#installation-by-manifest)
- [Documentation - Configuration](https://metallb.io/configuration/#layer-2-configuration)

## Ingress Nginx

```bash
helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx
helm repo update
helm search repo neginx 
helm template nginx ingress-nginx/ingress-nginx >test
helm upgrade --install --namespace ingess-nginx --create-namespace -f helm-tools/ingress-nginx/values.yaml ingress-nginx ingress-nginx/ingress-nginx
```

## Helmfile

- Installation:

  - [Download Helmfile](https://github.com/helmfile/helmfile/releases)

- Descompactando o binário

```bash
tar xvzf helmfile_0.171.0_linux_amd64.tar.gz 
```

- Instalando

```bash
./helmfile 
```

- Check da versão

```bash
./helmfile version
```

- MOvendo o binário para o diretório `bin`

```bash
sudo mv -f helmfile /usr/local/bin 
```

- Checando a localização

```bash
sudo mv -f helmfile /usr/local/bin 
```

- Check versão

```bash
helmfile version   
```

- Instalação dos plugins

```bash
helm plugin add https://github.com/databus23/helm-diff
helm plugin install https://github.com/jkroepke/helm-secrets --version v4.2.2
helm plugin install https://github.com/aslafy-z/helm-git --version 0.14.3
$ helm plugin install https://github.com/hypnoglow/helm-s3.git --version 0.14.0
```

- Uma outra opção : https://gist.github.com/genedy2017/142861e20a7c88b3ac7a78c86e09a5da

## Jenkins

- [Repositório](https://github.com/jenkinsci/helm-charts)
- [Velues utilizado](../helm-tools/jenkins/values.yaml)


- Get Password

```bash
kubectl get secret -n jenkins jenkins -ojson | jq -r '.data."jenkins-admin-password"' | base64 -d
```

## Gitea

- [Repositório](https://gitea.com/gitea/helm-chart/src/tag/v10.5.0/)
- [Values utilizado](../helm-tools/gitea/values.yaml)

- Expondo porta SSH do Gitea pelo Ingress
- O NGINX consegue expôr tanto serviços L4 quanto L7. Para isso vamos configurar o values do Chart:

```bash
tcp:
  22: "gitea/gitea-ssh:22"
```

- Com isso, o Service do NGINX vai abrir a porta 22, e qualquer requisição ali, será enviado para o Service gitea-ssh na namespace gitea.

## Harbor

- [Repositório](https://github.com/goharbor/harbor-helm/tree/v1.16.2)
- [Values utilizado](../helm-tools/harbor/values.yaml)

## Sonarqube
- [Repositório](https://github.com/SonarSource/helm-chart-sonarqube/tree/sonarqube-10.8.1-sonarqube-dce-10.8.1/charts/sonarqube)
- [Values utilizado](../helm-tools/sonarqube/values.yaml)

## Argocd
- [Repositório](https://github.com/argoproj/argo-helm/tree/argo-cd-7.8.2
- [Values utilizado](../helm-tools/argocd/values.yaml)


## Image Pull Secret Patcher
- [Repositório](https://artifacthub.io/packages/helm/empathyco/imagepullsecret-patcher)
- [Values utilizado](../helm-tools/imagepullsecret-patcher/)

## Kind
- Importando imagem para o cluster (Teste)

```bash
kind load docker-image restapi-flask:latest --name kind-workflow-pipe
```
