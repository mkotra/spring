# spring

This is a fully functional Spring Boot application that allows to pull radio station list provieded by https://www.radio-browser.info/

Radio stations will be saved in MongoDB and then can be returned. This is just for the purpose of this demo. 

Apart from the application logic which is quite simple, there are few nice features configured: 
- application starts on random port and registers in Consul service discovery
- live configuration reload is supported with Consul and @ScopeRefresh
- application exposes actuator, health checks and metrics including prometheus format
- unit and integration tests that can be written easily, and executed in the appropriate phases of maven build

To run everything You need: 
- Java 23
- MongoDB (8.0.0)
- Consul running on default ports
- Prometheus
- Docker (optional - it is required for unit tests and can also set up MongoDB, Consul and Prometheus)

Docker and `docker-compose.yml` file can be used to set up infrastructure.

``
docker compose up
``

seed consul values: 

``
./seed-consul.sh
``

run app:

``
mvn spring-boot:run
``

check Consul to make sure everything is up and running and determine APPLICATION_PORT

``
http://localhost:8500/
``

Swagger-UI can be used to test REST API: 

``
http://localhost:${APPLICATION_PORT}/swagger-ui/index.html
``

Metrics can be checked at:

``
http://localhost:${APPLICATION_PORT}/actuator/metrics
``

``
http://localhost:${APPLICATION_PORT}/actuator/prometheus
``

Prometheus dashboard is available here: 

``
http://localhost:9090  
``

Some example metrics can be used to test: 

``
 rate(pull_radio_stations_count_total[1m])
``

``
 pull_radio_stations_time_seconds_count
``

``
 pull_radio_stations_time_seconds_max
``

``
 pull_radio_stations_time_seconds_sum 
``

[![Java CI with Maven](https://github.com/mkotra/spring/actions/workflows/maven.yml/badge.svg)](https://github.com/mkotra/spring/actions/workflows/maven.yml)
