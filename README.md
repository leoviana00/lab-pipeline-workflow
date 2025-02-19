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

- [ ] Criar um Setup completo de um ambiente de desenvolvimento com cluster Kubernetes local usando Kind 
- [ ] Realizar o deploy de todas as ferramentas usando Helm Charts
- [ ] Automatizar o deploy de todos os Helm Charts usando Helmfile
- [ ] Configuração de repositórios, chaves e service users no Gitea
- [ ] Instalação de plugins como código no Jenkins
- [ ] Integração entre Gitea e Jenkins usando multibranch pipelines via webhook
- [ ] Transpondo testes unitários, lint e segurança para a pipeline
- [ ] Migração e utilização de Shared Libraries no Jenkins para modularidade
- [ ] Scan de código estático com report no SonarQube e bloqueio com Quality Gate
- [ ] Build de imagens Docker usando Kaniko com push para registry interno no Harbor
- [ ] Versionamento de artefatos seguindo o Gitflow
- [ ] Scan de segurança de imagens Docker usando scan on push do Harbor
- [ ] Promoção de artefatos usando o Crane
- [ ] Testes de integração com deploy no próprio cluster
- [ ] Deploy utilizando o princípio de GitOps via ArgoCD
- [ ] Separação de Deploy em Dev, Staging e Produção via regras de branches
- [ ] Notificação da pipeline [Serviço de notificação a definir ainda]

## Referências
- [Metallb](https://metallb.io/)
- [Descubra como o MetalLB aprimora o balanceamento de carga em ambientes Kubernetes on-premises](https://blog.4linux.com.br/instalando-e-configurando-o-metallb-em-um-ambiente-on-premises/)
- [Helmfile](https://helmfile.readthedocs.io/en/latest/#installation)


