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

You will need to set up your local proxy to work with the app. For nginx do:

    cd /usr/local/etc/nginx
    vi nginx.conf 
Then copy paste in the following:

    location ~ ^/profile/(api|management|swagger-resources|v2) {
        proxy_pass http://127.0.0.1:8082;
        proxy_set_header OIDC_access_token $token;
        proxy_pass_request_headers on;
    }

    location /profile {
        proxy_pass http://127.0.0.1:9082;
        proxy_pass_request_headers on;
    }

## Development

Before you can build this project, you must install and configure the following dependencies on your machine:

1. [Node.js][]: We use Node to run a development web server and build the project.
   Depending on your system, you can install Node either from source or as a pre-packaged bundle.
2. [Yarn][]: We use Yarn to manage Node dependencies.
   Depending on your system, you can install Yarn either from source or as a pre-packaged bundle.

After installing Node, you should be able to run the following command to install development tools.
You will only need to run this command when dependencies change in [package.json](package.json).

    yarn install

We use yarn scripts and [Webpack][] as our build system.

Before running the app you will need to create a database called "profile" in your local MySql.

Run the following commands in two separate terminals to create a blissful development experience where your browser
auto-refreshes when files change on your hard drive.

    ./mvnw
    yarn start

[Yarn][] is also used to manage CSS and JavaScript dependencies used in this application. You can upgrade dependencies by
specifying a newer version in [package.json](package.json). You can also run `yarn update` and `yarn install` to manage dependencies.
Add the `help` flag on any command to see how you can use it. For example, `yarn help update`.

The `yarn run` command will list all of the scripts available to run for this project.

## Building for production

To optimize the profile application for production, for the server side run:

    ./mvnw -Pprod clean package

For the UI run: 

    yarn prod

This will concatenate and minify the client CSS and JavaScript files. It will also modify `index.html` so it profiles these new files.
The ui build will be in the /ui-build folder in your workspace.
To ensure everything worked, run:

    java -jar target/*.war

And for the UI, if you have not done so already install http-server, then run: 

    npm install http-server -g

    http-server /Users/Alex/ws/hee_tis/TIS-CORE-SERVICES/ui-build -p 9082 -d false

With the proxy server up and configured, then navigate to [http://local.alxd.com/profile/](http://local.alxd.com/profile/) in your browser.

Refer to [Using JHipster in production][] for more details.

### Managing dependencies

For example, to add [Leaflet][] library as a runtime dependency of your application, you would run following command:

    yarn add --exact leaflet

To benefit from TypeScript type definitions from [DefinitelyTyped][] repository in development, you would run following command:

    yarn add --dev --exact @types/leaflet

Then you would import the JS and CSS files specified in library's installation instructions so that [Webpack][] knows about them:

Edit [src/main/webapp/app/vendor.ts](src/main/webapp/app/vendor.ts) file:
~~~
import 'leaflet/dist/leaflet.js';
~~~

Edit [src/main/webapp/content/css/vendor.css](src/main/webapp/content/css/vendor.css) file:
~~~
@import '~leaflet/dist/leaflet.css';
~~~

Note: there are still few other things remaining to do for Leaflet that we won't detail here.

For further instructions on how to develop with JHipster, have a look at [Using JHipster in development][].

### Using angular-cli

You can also use [Angular CLI][] to generate some custom client code.

For example, the following command:

    ng generate component my-component

will generate few files:

    create src/main/webapp/app/my-component/my-component.component.html
    create src/main/webapp/app/my-component/my-component.component.ts
    update src/main/webapp/app/app.module.ts

## Testing

To launch your application's tests, run:

    ./mvnw clean test

### Client tests

Unit tests are run by [Karma][] and written with [Jasmine][]. They're located in [src/test/javascript/](src/test/javascript/) and can be run with:

    yarn test



For more information, refer to the [Running tests page][].

## Using Docker to simplify development (optional)

You can use Docker to improve your JHipster development experience. A number of docker-compose configuration are available in the [src/main/docker](src/main/docker) folder to launch required third party services.
For example, to start a mysql database in a docker container, run:

    docker-compose -f src/main/docker/mysql.yml up -d

To stop it and remove the container, run:

    docker-compose -f src/main/docker/mysql.yml down

You can also fully dockerize your application and all the services that it depends on.
To achieve this, first build a docker image of your app by running:

    ./mvnw package -Pprod docker:build

Then run:

    docker-compose -f src/main/docker/app.yml up -d

For more information refer to [Using Docker and Docker-Compose][], this page also contains information on the docker-compose sub-generator (`yo jhipster:docker-compose`), which is able to generate docker configurations for one or several JHipster applications.

## Continuous Integration (optional)

To configure CI for your project, run the ci-cd sub-generator (`yo jhipster:ci-cd`), this will let you generate configuration files for a number of Continuous Integration systems. Consult the [Setting up Continuous Integration][] page for more information.

[JHipster Homepage and latest documentation]: https://jhipster.github.io
[JHipster 4.0.8 archive]: https://jhipster.github.io/documentation-archive/v4.0.8

[Using JHipster in development]: https://jhipster.github.io/documentation-archive/v4.0.8/development/
[Using Docker and Docker-Compose]: https://jhipster.github.io/documentation-archive/v4.0.8/docker-compose
[Using JHipster in production]: https://jhipster.github.io/documentation-archive/v4.0.8/production/
[Running tests page]: https://jhipster.github.io/documentation-archive/v4.0.8/running-tests/
[Setting up Continuous Integration]: https://jhipster.github.io/documentation-archive/v4.0.8/setting-up-ci/


[Node.js]: https://nodejs.org/
[Yarn]: https://yarnpkg.org/
[Webpack]: https://webpack.github.io/
[Angular CLI]: https://cli.angular.io/
[BrowserSync]: http://www.browsersync.io/
[Karma]: http://karma-runner.github.io/
[Jasmine]: http://jasmine.github.io/2.0/introduction.html
[Protractor]: https://angular.github.io/protractor/
[Leaflet]: http://leafletjs.com/
[DefinitelyTyped]: http://definitelytyped.org/
