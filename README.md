# taxi-camel-app-try-one

This API project was generated using MS3's [Camel OpenAPI Archetype](https://github.com/MS3Inc/camel-archetypes), version 0.2.7.

## Kubernetes

### Prerequisites:

* Create a folder with path: d/tmp/mysql/data.
* If your database has other password and username than:  password, root, then you should change
it application.yaml and secretMap.yaml files.
* If your database uses other port than 3306, you should change it in files: config-map.yaml, persistent-volume.yaml,
application.yaml.
* Install on your machine Docker and enable there Kubernetes.

### Process of starting application with Kubernetes (no Ingress):

1) Make sure kubernetes is ready, type in the terminal: <br>```kubectl cluster-info ```
2) Now, when we know that kubernetes is working properly, you have to be in the same directory as your 
kubernetes files (.yaml), for me it is: <br>```D:\camel\camel-taxi-transform-try-1\taxi-camel-app-try-one\kubernetes>```

3) We are ready to start, first create secret keys, type in the terminal:  
```kubectl apply -f ./secretMap.yaml```  

4) Now, create configuration-map:  
```kubectl apply -f ./config-map.yaml```

5) We are ready to create database:  
```kubectl apply -f ./persistent-volume.yaml```

6) The database is not created instantly, it needs some time about 30 seconds. I recommend to wait
1 minute or more. After 1 minute, the last step is to create deployment: <br> 
```kubectl apply -f ./camel-deploy.yaml```

7) If everything works fine (again I recommend to wait about 1 minute), 
you should be able to send requests by this url: localhost:9000/api/...

8) *You can also check the state of your pods by typing:<br>  ```kubectl get pods```<br>
Correct state is: RUNNING
   

### Getting Started

**Running on the Command Line**

```
mvn spring-boot:run
```

<!-- 
**Running Locally using IDE**

This project uses Spring profiles, and corresponding taxi-camel-app-try-one-<env>.yaml files.

Use the following environment variables: 
   * ```spring.profiles.active=<env>```
   * ```spring.config.name=taxi-camel-app-try-one```

**Running on Command Line**

```
mvn spring-boot:run -Dspring-boot.run.profiles=<env> -Dspring-boot.run.arguments="--spring.config.name=taxi-camel-app-try-one"
```
-->

### Actuator Endpoints

To access the list of available Actuator endpoints, go to: http://localhost:8080/actuator or `{{url}}/actuator`

The available endpoints are as follows:

* `/health`
* `/metrics`
* `/info`

#### Metrics

List of available metrics can be found here: http://localhost:8080/actuator/metrics/

Add the metric name in `/metrics/<metric name>` to access the metric for that particular topic.

Sample metric: http://localhost:8080/actuator/metrics/jvm.memory.used

```
{
    "name": "jvm.memory.used",
    "description": "The amount of used memory",
    ...
}
```

### Contact

* Name (email)