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
	@kubectl apply -f manifests/harbor-credentials.yaml

create:
	@kind create cluster --name kind-workflow-pipe --config config.yaml  

up: create pre helm

destroy:
	@kind delete clusters kind-workflow-pipe

passwd:
	@echo "JENKINS:"
	@kubectl get secret -n jenkins jenkins -ojson | jq -r '.data."jenkins-admin-password"' | base64 -d 
	@echo "\nSONARQUBE"
	@echo "bs7&GoIQOs!YOHtc"
	@echo "ARGOCD"
	@kubectl get secret -n argocd argocd-initial-admin-secret -ojson | jq -r '.data.password' | base64 -d
	@echo "\nGITEA"
	@echo "user:gitea_admin"
	@echo "pass:r8sA8CPHD9!bt6d"