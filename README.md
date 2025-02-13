# Radio Browser Spring Boot Demo

This is a fully functional Spring Boot application that retrieves a list of radio stations from [Radio Browser API](https://www.radio-browser.info/).

The retrieved radio stations are stored in MongoDB and can be accessed later. This setup is for demonstration purposes.

## Features
- The application starts on a **random port** and registers itself with **Consul service discovery**.
- Application Exposes **Spring Boot Actuator** for health checks and metrics (including **Prometheus** format).
- **Fabio load balancer** provides access on port 9999.
- **Live configuration reload** is supported via **Consul** and `@RefreshScope`.
- Well-structured **unit and integration tests**, executed in appropriate Maven build phases.

## Prerequisites
To run the application, you will need:
- **Java 23**
- **Docker** (required for unit tests and can also set up MongoDB, Consul, Fabio, and Prometheus)
- **MongoDB 8.0.0**
- **Consul** (running on default ports)
- **Fabio** (load balancer)
- **Prometheus**

## Running the Application
### Setting Up Infrastructure
Use Docker and `docker-compose.yml` to set up the necessary infrastructure:
```sh
docker compose up
```

### Seeding Consul Values
Run the following script to seed values into Consul:
```sh
./seed-consul.sh
```

### Starting the Application
Start the Spring Boot application using Maven:
```sh
mvn spring-boot:run
```

### API Documentation
REST API can be tested using Swagger UI:
`http://localhost:9999/demo/swagger-ui/index.html`

## Debugging & Monitoring
### Consul Dashboard
Check Consul to ensure all services are running properly: `http://localhost:8500/`

### Fabio Load Balancer
Verify that the demo service is registered correctly in Fabio and accessible on port 9999:
`http://localhost:9998/`

### Metrics & Monitoring
Actuator metrics can be accessed here:
`http://localhost:9999/demo/actuator/metrics`
and here: `http://localhost:9999/demo/actuator/prometheus`

Prometheus dashboard is available here: `http://localhost:9090`

### Example Prometheus Queries
Use these queries to monitor application performance:

```scss
 rate(pull_radio_stations_count_total[1m])
```

```scss
 pull_radio_stations_time_seconds_count
```

```scss
 pull_radio_stations_time_seconds_max
```

```scss
 pull_radio_stations_time_seconds_sum 
```

[![Java CI with Maven](https://github.com/mkotra/spring/actions/workflows/maven.yml/badge.svg)](https://github.com/mkotra/spring/actions/workflows/maven.yml)
