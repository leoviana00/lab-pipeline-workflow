<h1 align="center"> CI/CD - Kubernetes </h1>

<p align="center">
  <img alt="Kubernetes" src="https://img.shields.io/static/v1?label=Kubernetes&message=Deploy&color=8257E5&labelColor=000000"  />
  <img alt="License" src="https://img.shields.io/static/v1?label=license&message=MIT&color=49AA26&labelColor=000000">
</p>

<p align="center">
  <a href="#projeto">Projeto</a>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
  <a href="#tecnologias">Tecnologias</a>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
  <a href="#roadmap">Roadmap</a>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
  <a href="#referências">Referências</a>
</p>

<p align="center">
  <img alt="CI/CD" src="images/cicd-kubernetes.svg">
</p>


## Projeto

- Subir uma infraestrutura robusta 100% em Kubernetes, testando algumas tecnologias e a integração entre elas assim como também entender quais problemas resolvem. 

## Tecnologias

- Kubernetes
- Kind
- Docker
- Jenkins
- Harbor
- Sonarqube
- Helm
- Gitea
- ArgoCD
- Kaniko

## Roadmap

- [x] Criar um Setup completo de um ambiente de desenvolvimento com cluster Kubernetes local usando Kind 
- [x] Realizar o deploy de todas as ferramentas usando Helm Charts
- [x] Automatizar o deploy de todos os Helm Charts usando Helmfile
- [x] Configuração de repositórios, chaves e service users no Gitea
- [x] Instalação de plugins como código no Jenkins
- [x] Integração entre Gitea e Jenkins usando multibranch pipelines via webhook
- [x] Transpondo testes unitários, lint e segurança para a pipeline
- [x] Migração e utilização de Shared Libraries no Jenkins para modularidade
- [x] Scan de código estático com report no SonarQube e bloqueio com Quality Gate
- [x] Build de imagens Docker usando Kaniko com push para registry interno no Harbor
- [x] Versionamento de artefatos seguindo o Gitflow
- [x] Scan de segurança de imagens Docker usando scan on push do Harbor
- [x] Promoção de artefatos usando o Crane
- [x] Testes de integração com deploy no próprio cluster

> [!IMPORTANT]  
> OBS: Etapa base da esteira de CI finalizada, porém está sujeita a vários pontos de melhorias, alguns exemplos são:
> Tirar o nome da aplicação na pipeline e mudar para variável;
> Remover senhas do charts e trabalhar com cofre de senha;
> Melhorar a estrutra de charts deixando mais genéricas;
> Tratar e trabalhar melhor as notificações;

- [ ] Deploy utilizando o princípio de GitOps via ArgoCD
- [ ] Separação de Deploy em Dev, Staging e Produção via regras de branches
- [ ] Notificação da pipeline [Serviço de notificação a definir ainda]

## Referências
- [Kubernetes Kind](https://kind.sigs.k8s.io/)
- [Metallb](https://metallb.io/)
- [Descubra como o MetalLB aprimora o balanceamento de carga em ambientes Kubernetes on-premises](https://blog.4linux.com.br/instalando-e-configurando-o-metallb-em-um-ambiente-on-premises/)
- [Ingress Nginx](https://github.com/kubernetes/ingress-nginx)
- [Helmfile](https://helmfile.readthedocs.io/en/latest/#installation)
- [Jenkins](https://www.jenkins.io/doc/)
- [Plugin Jenkins Multibranch Scan Webhook](https://plugins.jenkins.io/multibranch-scan-webhook-trigger/)
- [Plugin Jenkins KUbernetes](https://plugins.jenkins.io/kubernetes/)
- [Plugin Jenkins Discord Notifier](https://plugins.jenkins.io/discord-notifier/)
- [Plugin Jenkins Basic Branch Build Strategies](https://plugins.jenkins.io/basic-branch-build-strategies/)
- [Gitea](https://gitea.com/gitea/helm-chart)
- [Harbor](https://goharbor.io/docs/2.4.0/)
- [Unable to load local docker image in kind kubernetes cluster](https://stackoverflow.com/questions/73962956/unable-to-load-local-docker-image-in-kind-kubernetes-cluster)
- [Share a standard Pipeline across multiple projects with Shared Libraries](https://www.jenkins.io/blog/2017/10/02/pipeline-templates-with-shared-libraries/)
- [Extending with Shared Libraries](https://www.jenkins.io/doc/book/pipeline/shared-libraries/)
- [SonarScanner CLI](https://docs.sonarsource.com/sonarqube-server/10.8/analyzing-source-code/scanners/sonarscanner/)
- [Trivy](https://github.com/aquasecurity/trivy)
- [Harbor Vulnerability Scanning](https://goharbor.io/docs/2.0.0/administration/vulnerability-scanning/)
- [Crane](https://github.com/google/go-containerregistry/blob/main/cmd/crane/doc/crane.md)