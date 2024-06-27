# spring

You need Java 22, MongoDB (at least 5.0.0) and Consul running on default ports: 


Mongo (with Docker)

``
docker run --name mongodb -p 27017:27017 -d mongodb:5.0.0
``

Consul:

``
consul agent -dev
``

seed consul values: 

``
./seed-consul.sh
``

Run app:

``
mvn spring-boot:run
``


[![Java CI with Maven](https://github.com/mkotra/spring/actions/workflows/maven.yml/badge.svg)](https://github.com/mkotra/spring/actions/workflows/maven.yml)
