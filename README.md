1. Gallery Service

[./gallery/]

> ./mvnw compile quarkus:dev

> ./mvnw package

2. Docker Build

[./]

> docker-compose build

> docker-compose push

> docker run -i --rm -p 8080:8080 fax4ever/gallery:1.0.0-SNAPSHOT

3. Kubernetes Deployment

[./]

> kubectl create namespace image-caption-generation

> kubectl apply -f gallery/target/kubernetes/kubernetes.yml --namespace=image-caption-generation

> kubectl delete namespace image-caption-generation