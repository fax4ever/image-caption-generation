# Image Caption Generation

See extra documentation [here](extra/README.md)
In particular to see how set up an Ingress controller

## Build and deploy cycle

The parts from **7** to **12** can be skipped if you don't want update the container images
The part **15** is only to apply some changes to the cluster if you redeploy and publish a new image of the application

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
helm install -f infinispan.yaml -n image-caption-generation infinispan openshift/infinispan --version 0.3.2
```

7. **Optionally** Create some port forwarding

For instance if the Kubernetes does not have a LoadBalancer service.

``` shell
kubectl port-forward services/img-rabbitmq 15672
```

``` shell
kubectl port-forward services/infinispan 11222
```

8. **Optionally** Compile the Java/Quarkus `gallery` service: 

``` shell
mvn -f ./gallery/pom.xml clean package
```

9. **Optionally** Compile the Java/Quarkus `user` service:

``` shell
mvn -f ./users/pom.xml clean package
```

10. **Optionally** Compile the Angular web application:

[./webapp/] <-- run from this subdir

``` shell
ng build --configuration production
```

11. **Optionally** Build the Docker images locally:

``` shell
docker-compose build
```

12. **Optionally** Push the Docker images to the Docker remote repository:

``` shell
docker-compose push
```

13. Deploy the application, Kubernetes will pull the images from the remote repository:

``` shell
kubectl apply -f kubernetes.yaml
```

14. See all the pods starting...

``` shell
kubectl get pods -w
```

15. **Optionally** redeploy a service

``` shell
kubectl rollout restart deployment caption
```

## Play with the App

This demo is executable without the need of a webapp implemented.

1. Crate users

```
http POST http://localhost/users/user <<<'{"name":"norman", "pass":"norman", "pro":true}'
```

```
http POST http://localhost/users/user <<<'{"name":"victor", "pass":"victor", "pro":false}'
```

2. Verify login service

```
http POST http://localhost/users/user/validate <<<'{"username":"fabio", "password":"fabio"}'
```

```
http POST http://localhost/users/user/validate <<<'{"username":"norman", "password":"wrong"}'
```

```
http POST http://localhost/users/user/validate <<<'{"username":"norman", "password":"norman"}'
```

```
http POST http://localhost/users/user/validate <<<'{"username":"victor", "password":"victor"}'
```

3. Publish images

Go to the page http://localhost/caption/new-image/norman and insert some images
In this case the images are generated for the user `norman`.

Go to the page http://localhost/caption/new-image/victor and insert some images
In this case the images are generated for the user `victor`.

4. Verify that images are stored and are retrievable from the caption service.

Go to the page http://localhost/caption/image/victor/empire-state-building.png

5. Find all images belonging to `norman` / `victor`

```
http http://localhost/gallery/image/user/norman
```

```
http http://localhost/gallery/image/user/victor
```
6. Find all images having a capture containing the term `cat` / `clock`

```
http http://localhost/gallery/image/caption/cat
```

```
http http://localhost/gallery/image/caption/clock
```

7. Test that the webapp is reachable

Navigate to http://localhost/