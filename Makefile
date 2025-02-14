.DEFAULT_GOAL := create

create:
	@kind create cluster --name kind-workflow-pipe --config config.yaml  

destroy:
	@kind delete clusters kind-workflow-pipe