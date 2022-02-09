# SQL Tool
A tool meant to speed up development of web applications by generating a large amount of the web server code.
The code is generated in a layered structure, i.e. Controller, Service, Repository, Mapper, View, Entity <br />
You can find the tool here <a href="https://mossonthetree-codegenweb.azurewebsites.net/">code gen</a> <br />

## Generated code
- Entities managed by the ORM system
- Views that will be sent to and from clients
- Controllers (handle HTTP requests)
- Services (handle domain logic and translating between view and entity)
- Repositories (interface to underlying data storage, there are in-memory and database versions)
- Mappers (convert entities to views and vice versa)
- Axios http calls from javascript
- Http calls from Flutter code
- Kafka producers, consumers, serializers and deserializers for Java
- ElasticSearch clients

## Using the code
### Data types
- int
- boolean
- string
- char
- date
- guid
### Options
- primary (marks column as primary field)
- secondary (marks the column as secondary field, used only in linking tables)
- auto_increment (works only for ints)
- unique (marks the column as unique, will provide "select by unique" functionality)
- foreign (marks the column as a foreign key, required the name of the parent table in parentheses)

## Running the program
There are a few build.sh scripts in the various folders:
- codegenerator/build.sh
- codegeneratorweb/build.sh
- kubernetes/build.sh
These can be used to build the projects, and create docker images. These images are then used by Kubernetes

## Running the projects
I use Minikube as the Kubernetes cluster, and the images are built into the docker environment of Minikube
```
minikube start
```
```
eval $(minikube -p minikube docker-env)
```
```
cd codegenerator
./build.sh
```
```
cd ..
cd codegeneratorweb
./build.sh
```
```
cd ..
cd kubernetes
./build.sh
```
Also note, you will need to add the hostname to your /etc/hosts file for the ingress to work.
