# Image Caption Generation

See extra documentation [here](extra/README.md)
In particular to see how set up an Ingress controller

## Build and deploy cycle

The parts from **7** to **11** can be skipped if you don't want update the container images
The part **14** is only to apply some changes to the cluster if you redeploy and publish a new image of the application

1. Update the source code

``` shell
git pull
```

2. Make `image-caption-generation` the working namespace

``` shell
kubectl config set-context --current --namespace=image-caption-generation
```

3. Remove any previous deployment

``` shell
kubectl delete namespace image-caption-generation
```

4. Recreate an empty namespace for the new deployment

``` shell
kubectl create namespace image-caption-generation
```

5. Install RabbitMQ. Notice that you need to install the [bitnami](https://charts.bitnami.com/bitnami) repository before.

``` shell
helm install -f rabbitmq.yaml -n image-caption-generation img-rabbitmq bitnami/rabbitmq --version 12.12.0
```

6. Install Infinispan. Notice that you need to install [openshift](https://charts.openshift.io/) repository before. 

``` shell
helm install -f infinispan.yaml -n image-caption-generation infinispan openshift/infinispan-infinispan --version 0.3.2
```

7. **Optionally** Compile the Java/Quarkus `gallery` service: 

``` shell
mvn -f ./gallery/pom.xml clean package
```

8. **Optionally** Compile the Java/Quarkus `user` service:

``` shell
mvn -f ./users/pom.xml clean package
```

9. **Optionally** Compile the Angular web application:

[./webapp/] <-- run from this subdir

``` shell
ng build --configuration production
```

10. **Optionally** Build the Docker images locally:

``` shell
docker-compose build
```

11. **Optionally** Push the Docker images to the Docker remote repository:

``` shell
docker-compose push
```

12. Deploy the application, Kubernetes will pull the images from the remote repository:

``` shell
kubectl apply -f kubernetes.yaml
```

13. See all the pods starting...

``` shell
kubectl get pods -w
```

14. **Optionally** redeploy a service

``` shell
kubectl rollout restart deployment caption
```
