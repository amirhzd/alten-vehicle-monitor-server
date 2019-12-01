# Alten Vehicle Monitor (server)

This application was generated using JHipster 6.1.0, you can find documentation and help at [https://www.jhipster.tech/documentation-archive/v6.1.0](https://www.jhipster.tech/documentation-archive/v6.1.0).

## Development

To start the application in the dev profile, simply run:

    ./mvnw


## Building for production

### Packaging as jar

To build the final jar and optimize the alten-vehicle-monitor-server application for production, run:

    ./mvnw -Pprod clean package

To ensure everything worked, run:

    java -jar target/*.jar


### Packaging as war

To package the application as a war in order to deploy it to an application server, run:

    ./mvnw -Pprod,war clean package

## Testing

To launch the application's tests, run:

    ./mvnw verify


## Using Docker to simplify development

A number of docker-compose configuration are available in the [src/main/docker](src/main/docker) folder to launch required third party services.

To start a postgresql database in a docker container, run:

    docker-compose -f src/main/docker/postgresql.yml up -d

To stop it and remove the container, run:

    docker-compose -f src/main/docker/postgresql.yml down
    
To start a rabbitmq in a docker container, run:
    
    docker-compose -f src/main/docker/rabbitmq.yml up -d

To stop it and remove the container, run:

    docker-compose -f src/main/docker/rabbitmq.yml down

You can also fully dockerize the application and all the services that it depends on.
To achieve this, first build a docker image of the app by running:

    ./mvnw -Pprod package jib:dockerBuild

Then run:

    docker-compose -f src/main/docker/app.yml up -d
