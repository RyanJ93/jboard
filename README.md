# jBoard

jBoard is a web application for online lessons management developed as an academic project using Vue, Java and Java Servlets. It is designed to support admin users, who can add new teachers, courses and then lessons, and regular users, who can book lessons.
<br />
A native Java based Android application is available too, more information [here](https://github.com/RyanJ93/jboard-android).
<br />
You can try out an online instance of this application [here](http://jboard.enricosola.dev).

## Requirements

- Java 15 or greater
- Gradle 7
- A Java Servlet Server, Tomcat 9 is recommended.
- MySQL 8
- Node.js 14 or greater

## Configuration

Before starting up the application you need to add the configuration file, you can find a sample configuration file in the `config` directory, you can duplicate that file, edit it with your database connection information and then rename it as `config.json`. Also, you can set up Sentry error tracking by defining your own Sentry DSN URL in the configuration file. <br />
Additionally, you need to import the database structure, you can find the whole structure contained in the `resources/database.sql` file.

## Building the front-end

The front-edn side is managed using NPM so first of all you need to install required dependencies running the `npm i` command in the project root directory. Once dependencies have successfully been installed you have to compile the whole font-end part using the following command:

```bash
npm run webpack-dev
```

Or if you need a minified version of the front-end code you can run this command:

```bash
npm run webpack-prod
```

## Building the application

Once the front-end side is compiled and ready you need to build the .war archive before deploying it onto a Java Servlet Server, such as Tomcat. The whole web application's dependencies and building strategy is managed by Gradle so make sure you have Gradle 7 and Java 15 or greater installed on your machine and then all you need is to run the following command:

```bash
gradle war
```

This command will produce a war archive located at `build/libs/jboard.war`.

### jBoard as a Docker image

You can deploy this project as a Docker container: you can simply run the pre-built image that you can find [here](https://hub.docker.com/repository/docker/enricosola/jboard) or if you prefer you can build your own image by running the `docker build` command inside the project root directory.

## User management commands

Project provides some CLI commands to manage users, all those commands must be executed from the project root path:

- Add a new user: `npm run useradd`
- Delete a user: `npm run userdel`
- List all the registered users: `npm run userlist`
- Change a user password: `npm run change-user-password`

If your're running the application as a docker container you must use the following alternatives:

- useradd: `docker exec -it [CONTAINER NAME] jboard-useradd`
- userdel: `docker exec -it [CONTAINER NAME] jboard-userdel`
- userlist: `docker exec -it [CONTAINER NAME] jboard-userlist`
- change-user-password: `docker exec -it [CONTAINER NAME] jboard-change-user-password`

Application server tested on Apple macOS and RedHat Enterprise Linux 8, currently not tested on Microsoft Windows.

Developed with ❤️ by [Enrico Sola](https://www.enricosola.dev).