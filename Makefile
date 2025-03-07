.DEFAULT_GOAL := create

pre:
	@kubectl apply -f https://raw.githubusercontent.com/metallb/metallb/v0.14.3/config/manifests/metallb-native.yaml
	@kubectl wait --namespace metallb-system \
		--for=condition=ready pod \
		--selector=app=metallb \
		--timeout=300s

	@kubectl apply -f manifests/setup-hosts.yaml
	@kubectl apply -f manifests/metallb-pool.yaml

helm:
	@helmfile apply
	@kubectl apply -f manifests/harbor-credentials-jenkins.yaml
	@kubectl apply -f manifests/harbor-credentials-secretspatcher.yaml
	@kubectl apply -f manifests/jenkins-rbac.yaml

create:
	@kind create cluster --name kind-workflow-pipe --config config.yaml  

up: create pre helm

destroy:
	@kind delete clusters kind-workflow-pipe

passwd:
	@echo "JENKINS:"
	@kubectl get secret -n jenkins jenkins -ojson | jq -r '.data."jenkins-admin-password"' | base64 -d 
	@echo "\n"

	@echo "\nSONARQUBE"
	@echo "bs7&GoIQOs!YOHtc"
	@echo "\n"

	@echo "ARGOCD"
	@kubectl get secret -n argocd argocd-initial-admin-secret -ojson | jq -r '.data.password' | base64 -d
	@echo "\n"

	@echo "GITEA"
	@echo "user:gitea_admin"
	@echo "pass:r8sA8CPHD9!bt6d"
	@echo "\n"

	@echo "GRAFANA:"
	@echo "admin"
	@kubectl get secret -n monitoring kube-prometheus-stack-grafana -ojson | jq -r '.data."admin-password"' | base64 -d
	@echo "\n"
