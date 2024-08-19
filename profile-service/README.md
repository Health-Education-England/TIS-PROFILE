# TIS PROFILE

It was created using Jhipster and then modifying the generated code to the structure we need. 
Some of the things we did where:

- removed the security related code, we are substituting out own
- removed the auditing code, we are using our own
- replaced liquibase with flyway
- made the UI and server side into two completely separate apps
- refactored code to match our desired cosmetics

Please delete this paragraph once you copy and use the template. 
Details on how to use this here: https://hee-tis.atlassian.net/wiki/display/TISDEV/How+to+morph+a+Jhipster+scaffolded+app

## Setting up local proxy paths

The simplest way is to use the local Admin UI setup in your development handbook.

### Dev Handbook


### Running directly on your machine
You will need to set up your local proxy to work with the app. For nginx do:

    cd /usr/local/etc/nginx
    vi nginx.conf 

Then copy paste in the following:

    location ~ ^/profile/ {
        proxy_pass http://127.0.0.1:8082;
        proxy_set_header OIDC_access_token $token;
        proxy_pass_request_headers on;
    }

## Development

Before you can build this project, you must install and configure standard dependencies on your machine:

- [JDK]
- Before running the app you will need to create a database called "profile" in your local MySql.

Run the following command in a terminal to create a blissful development experience.

    ./mvnw

## Building for production

We build for production using CI/CD pipelines.
Should you need to build locally, optimize the profile application for production.
Build the service:

    ./mvnw -Pprod clean package

To ensure everything worked, run:

    java -jar target/*.war

With the proxy server up and configured, then navigate to [http://localhost/profile/](http://localhost/profile/) in your browser.

Refer to the development handbook for more details.

## Testing

To launch your application's tests, run:

    ./mvnw clean test

## Using Docker to simplify development (optional)

You may build a docker container by using the `docker build` command in the ci-cd-workflow.
Set or replace the environment variables in the image name. 

## Continuous Integration

This project uses GitHub Actions for CI/CD.  It does not build every push.

[JDK]: https://adoptium.net/en-GB/temurin/releases/?version=11