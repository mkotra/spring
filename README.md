# spring

You need: 
- Java 22
- MongoDB (at least 5.0.0)
- Consul running on default ports: 

Docker and `docker-compose.yml` file can be used. 

``
docker-compose up
``

seed consul values: 

``
./seed-consul.sh
``

Run app:

``
mvn spring-boot:run
``

Check Consul to make sure everything is up and running and determine APPLICATION_PORT

``
http://localhost:8500/
``

Swagger-UI can be used to test REST API: 

``
http://localhost:${APPLICATION_PORT}/
``




[![Java CI with Maven](https://github.com/mkotra/spring/actions/workflows/maven.yml/badge.svg)](https://github.com/mkotra/spring/actions/workflows/maven.yml)
