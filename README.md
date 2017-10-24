# MyTasks Server

Server side of a toy web application I'm writing as a learning exercise.  It's a variation on the traditional 'todo' application, but with some of the features of an issue tracker.

This part uses [Spring Boot / Spring Web MVC](https://projects.spring.io/spring-boot/) and [Hibernate](http://hibernate.org) with an embedded [H2 database](http://h2database.com/html/main.html).

## Developing

Use [this custom check style configuration](https://bitbucket.org/crimes/mytasks/downloads/custom-sun-checks.xml).

## Development server

Run `mvn -D run.profiles=dev spring-boot:run` to run in development mode (in-memory database with some sample data). The REST API is rooted at `http://localhost:8080/api`.

## Running in Docker

### Building the image

    docker build -t mytasks-server:latest .

### Running the server

To create and run the container (replacing the `%MY_***%` placeholders with your own credentials):

    docker volume create mytasks-data
    docker run -d -p 8080:8080 -v mytasks-data:/mytasks -e "AUTH0_DOMAIN=%MY_DOMAIN%" -e "AUTH0_ISSUER=%MY_ISSUER%" -e "AUTH0_CLIENTID=%MY_CLIENT%" -e "AUTH0_CLIENTSECRET=%MY_SECRET%" --name mytasks-server --restart=always mytasks-server:latest

### Enabling TLS

To build a Docker image with support for TLS, perform the following steps before building the image.

#### Create a self-signed certificate

Use `keytool` to generate a self-signed certificate in the root of the repository:

    keytool -genkey -alias mytasks -keyalg RSA -keysize 2048 -keystore keystore.pks -validity 3650

#### Configure TLS

Add the following to `application.yml` in the repository root (creating the file if necessary), filling in the passwords used to create the certificate:

    server:
      port: 443
      ssl:
        enabled: true
        key-alias: mytasks
        key-store: /keystore.jks
        key-store-type: JKS
        key-store-password: %STORE_PASSWORD%
        key-password: %KEY_PASSWORD%

As the server is now listening on a different port, it's necessary to change the argument to the `docker run` command above to `-p 8443:443`.

#### Import the certificate

Use `keytool` to extract the certificate from the key store:

    keytool -export -alias mytasks -file mytasks.crt -keystore keystore.jks

What you need to do with the resulting file is OS-specific; on MacOS run e.g.:

    sudo security add-trusted-cert -d -r trustRoot -k ~/Library/Keychains/login.keychain mytasks.crt

