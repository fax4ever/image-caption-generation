# Image Caption Generation

## Clone

``` shell
git lfs install
```

> sudo dnf install git-lfs

``` shell
git clone git@github.com:fax4ever/image-caption-generation.git
```

## Helm Chart

```
helm repo add openshift-helm-charts https://charts.openshift.io/
```

## Kubernetes: Deploy

[./]

``` shell
kubectl create namespace image-caption-generation
```
> kubectl delete namespace image-caption-generation

``` shell
kubectl config set-context --current --namespace=image-caption-generation
```

``` shell
helm install -f infinispan.yaml -n image-caption-generation infinispan openshift-helm-charts/infinispan-infinispan
```

> helm upgrade -f infinispan.yaml -n image-caption-generation infinispan openshift-helm-charts/infinispan-infinispan
 
> helm uninstall infinispan
 
> kubectl delete secret infinispan-generated-secret

``` shell
kubectl get pods -w
```

``` shell
kubectl apply -f kubernetes.yaml
```

``` shell
kubectl get all
```

``` shell
kubectl wait pod --all --for=condition=Ready --namespace=${ns}
```

``` shell
IP=$(kubectl get service caption -o jsonpath="{.status.loadBalancer.ingress[0].ip}")
PORT=$(kubectl get service caption -o jsonpath="{.spec.ports[0].port}")
echo $IP:$PORT
```

``` shell
IP=$(kubectl get service gallery -o jsonpath="{.status.loadBalancer.ingress[0].ip}")
PORT=$(kubectl get service gallery -o jsonpath="{.spec.ports[0].port}")
echo $IP:$PORT
```

``` shell
IP=$(kubectl get service webapp -o jsonpath="{.status.loadBalancer.ingress[0].ip}")
PORT=$(kubectl get service webapp -o jsonpath="{.spec.ports[0].port}")
echo $IP:$PORT
```

``` shell
kubectl logs services/caption 
```

``` shell
kubectl logs services/gallery 
```

``` shell
kubectl logs services/webapp 
```

## Gallery Service: Develop and Package

[./gallery/]

``` shell
./mvnw compile quarkus:dev
```

``` shell
./mvnw package
```

## WebApp: Develop and Package

[./weapp/]

``` shell
ng serve
```

``` shell
ng build --configuration production
```

## Docker: Build and Publish

[./]

``` shell
docker-compose build
```

``` shell
docker-compose push
```

``` shell
docker run -i --rm -p 8080:8080 --name=gallery docker.io/fax4ever/gallery:1.0.0-SNAPSHOT
```

``` shell
docker run -i --rm -p 8000:8000 --name=caption docker.io/fax4ever/caption:1.0.0-SNAPSHOT
```

``` shell
docker run -i --rm -p 80:80 --name=webapp docker.io/fax4ever/webapp:1.0.0-SNAPSHOT
```

``` shell
docker exec -it webapp /bin/sh
```

``` shell
docker build -t docker.io/fax4ever/test:1.0.0-SNAPSHOT .
```

## Install Bare Metal Kubernetes: Kind + MetalLB

``` shell
sudo systemctl start docker
```

> docker info

``` shell
kind create cluster --name=blablabla
```

> kind get clusters

> kubectl cluster-info --context kind-blablabla

> kind delete cluster --name=blablabla

(copied from https://raw.githubusercontent.com/metallb/metallb/v0.13.7/config/manifests/metallb-native.yaml)
``` shell
kubectl apply -f extra/metallb-native.yaml
```

``` shell
kubectl wait --namespace metallb-system \
--for=condition=ready pod \
--selector=app=metallb \
--timeout=90s
```

``` shell
kubectl create namespace image-caption-generation
```
> kubectl delete namespace image-caption-generation

``` shell
kubectl config set-context --current --namespace=image-caption-generation
```

``` shell
docker network inspect -f '{{.IPAM.Config}}' kind
```

if ≠ 172.18.0.0/16
  modify with IP address [./extra/metallb-config.yaml]
```
kubectl apply -f extra/metallb-config.yaml
```
