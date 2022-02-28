# codegenerator

This is a code generator application for Java springboot and Python Django apps. It's implemented as a restful springboot application with React front end. There are various scripts included to create kubernetes deployments to be run inside a cluster. I used minikube while developing and testing the app.

## Generated items
- Springboot apps
- Django apps
- Kafka clients
- Elasticsearch clients

## project structure
The generated apps are created based on a definition provided. This is implemented as a definition language to define the business objects, and based on these object definitions, various layers of classes will be generated. What is comes down to is a controller layer, service layer, data access interface layer and business object plain old objects. 

## Database
The data access interfaces support SQL database access to MySql or in memory lists.

## Planned features
- Apache pulsar clients
- Redis cluster interfaces
