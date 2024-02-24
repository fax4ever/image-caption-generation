# Image Capture Generation

## Kubernetes: Deploy

[./]

> kubectl apply -f kubernetes.yaml

> kubectl get all

> IP=$(kubectl get service gallery -o jsonpath="{.status.loadBalancer.ingress[0].ip}")
PORT=$(kubectl get service gallery -o jsonpath="{.spec.ports[0].port}")
echo $IP:$PORT

## Gallery Service: Develop and Compile

[./gallery/]

> ./mvnw compile quarkus:dev

> ./mvnw package

## Docker: Build and Publish

[./]

> docker-compose build

> docker-compose push

> docker run -i --rm -p 8080:8080 fax4ever/gallery:1.0.0-SNAPSHOT

## Install Bare Metal Kubernetes: Kind + MetalLB

> sudo systemctl start docker
[docker info]

> kind create cluster --name=blablabla
[kind get clusters]
[kubectl cluster-info --context kind-blablabla]
[kind delete cluster --name=blablabla]

(copied from https://raw.githubusercontent.com/metallb/metallb/v0.13.7/config/manifests/metallb-native.yaml)
> kubectl apply -f extra/metallb-native.yaml

> kubectl wait --namespace metallb-system \
--for=condition=ready pod \
--selector=app=metallb \
--timeout=90s

> kubectl create namespace image-caption-generation
[kubectl delete namespace image-caption-generation]

> kubectl config set-context --current --namespace=image-caption-generation

> docker network inspect -f '{{.IPAM.Config}}' kind

=> modify with IP address [./extra/metallb-config.yaml]
> kubectl apply -f extra/metallb-config.yaml

