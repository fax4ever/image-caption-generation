# Image Caption Generation (Extra documentation)

## Clone

``` shell
git lfs install
```

> sudo dnf install git-lfs

``` shell
git clone git@github.com:fax4ever/image-caption-generation.git
```

## Helm Charts

```
helm repo add openshift https://charts.openshift.io/
```

```
helm repo add bitnami https://charts.bitnami.com/bitnami
```

```
helm repo add nginx https://helm.nginx.com/stable
```

> helm repo list

> helm repo remove openshift

## Kubernetes: Deploy

[./]

``` shell
kubectl config set-context --current --namespace=image-caption-generation
```

``` shell
kubectl delete namespace image-caption-generation
```

``` shell
kubectl create namespace image-caption-generation
```

### RabbitMQ

It is crucial to NOT use `rabbitmq` as Kube service name. 
See: https://stackoverflow.com/questions/67491221/numberformatexception-when-starting-quarkus-on-kubernetes

``` shell
helm install -f rabbitmq.yaml -n image-caption-generation img-rabbitmq bitnami/rabbitmq --version 12.12.0
```

> helm delete img-rabbitmq

``` shell
kubectl get pods -w
```

``` shell
IP=$(kubectl get service img-rabbitmq -o jsonpath="{.status.loadBalancer.ingress[0].ip}")
echo $IP
```

Use `$IP` in place of `172.18.255.200`
``` shell
http://172.18.255.200:15672
```
* Username: admin
* Password: admin

### Infinispan

``` shell
helm install -f infinispan.yaml -n image-caption-generation infinispan openshift/infinispan-infinispan --version 0.3.2
```

> helm upgrade -f infinispan.yaml -n image-caption-generation infinispan openshift/infinispan-infinispan
 
> helm delete infinispan
 
> kubectl delete secret infinispan-generated-secret

``` shell
kubectl get pods -w
```

``` shell
IP=$(kubectl get service infinispan -o jsonpath="{.status.loadBalancer.ingress[0].ip}")
PORT=$(kubectl get service infinispan -o jsonpath="{.spec.ports[0].port}")
echo $IP:$PORT
```

Use `$IP:$PORT` in place of `172.18.255.201:11222`
``` shell
http://172.18.255.201:11222
```
* Username: admin
* Password: admin

### Application

``` shell
kubectl apply -f kubernetes.yaml
```

``` shell
kubectl get all
```

``` shell
kubectl wait pod --all --for=condition=Ready --namespace=${ns}
```

#### Caption Service

``` shell
kubectl logs services/caption 
```

``` shell
IP=$(kubectl get service caption -o jsonpath="{.status.loadBalancer.ingress[0].ip}")
PORT=$(kubectl get service caption -o jsonpath="{.spec.ports[0].port}")
echo $IP:$PORT
```

Use `$IP:$PORT` in place of `172.18.255.202:8000`:
``` web
http://172.18.255.202:8000/new-image/ciao
```

```
kubectl rollout restart deployment caption
```

#### Gallery Service

``` shell
kubectl logs services/gallery 
```

``` shell
IP=$(kubectl get service gallery -o jsonpath="{.status.loadBalancer.ingress[0].ip}")
PORT=$(kubectl get service gallery -o jsonpath="{.spec.ports[0].port}")
echo $IP:$PORT
```

Use `$IP:$PORT` in place of `172.18.255.203:8080`:
``` web
http://172.18.255.203:8080/image/cache
```

```
kubectl rollout restart deployment gallery
```

#### Web Application

``` shell
kubectl logs services/webapp 
```

``` shell
IP=$(kubectl get service webapp -o jsonpath="{.status.loadBalancer.ingress[0].ip}")
PORT=$(kubectl get service webapp -o jsonpath="{.spec.ports[0].port}")
echo $IP:$PORT
```

Use `$IP:$PORT` in place of `172.18.255.204:80`:
``` web
http://172.18.255.204:80
```

```
kubectl rollout restart deployment webapp
```

#### Users Service

``` shell
kubectl logs services/users 
```

``` shell
IP=$(kubectl get service users -o jsonpath="{.status.loadBalancer.ingress[0].ip}")
PORT=$(kubectl get service users -o jsonpath="{.spec.ports[0].port}")
echo $IP:$PORT
```

Use `$IP:$PORT` in place of `172.18.255.203:9000`:
``` web
http://172.18.255.203:9000/user/cache
```

```
kubectl rollout restart deployment gallery
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
docker run -i --rm -p 80:80 --name=test docker.io/fax4ever/test:1.0.0-SNAPSHOT
```

``` shell
docker exec -it webapp /bin/sh
```

``` shell
docker build -t docker.io/fax4ever/test:1.0.0-SNAPSHOT .
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
docker network inspect -f '{{.IPAM.Config}}' kind
```

if ≠ 172.18.0.0/16
  modify with IP address [./extra/metallb-config.yaml]
```
kubectl apply -f extra/metallb-config.yaml
```

## Install Bare Metal Kubernetes: Kind + Ingress NGINX

If you want to create a Kind cluster ready for the Ingress.

``` shell
kind create cluster --name=blablabla --config extra/kind-ingress-ready.yaml
```

(copied from https://raw.githubusercontent.com/kubernetes/ingress-nginx/main/deploy/static/provider/kind/deploy.yaml)
``` shell
kubectl apply -f extra/ingress-nginx.yaml
```

``` shell
kubectl wait --namespace ingress-nginx \
  --for=condition=ready pod \
  --selector=app.kubernetes.io/component=controller \
  --timeout=90s
```

##  Install Ingress NGINX with Helm

```
helm install nginx-ingress nginx-ingress --version 1.1.3 -n nginx-ingress --repo https://helm.nginx.com/stable --create-namespace
```

## Test Ingress controller

```
kubectl delete namespaces test
```

``` shell
kubectl create namespace test
```

(copied and adapted from https://kind.sigs.k8s.io/examples/ingress/usage.yaml)
``` shell
kubectl apply -f extra/ingress-usage.yaml -n test
```

``` shell
http localhost/foo/hostname
```