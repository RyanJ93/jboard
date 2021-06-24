FROM gradle:7.0.2-jdk16 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN curl -sL https://deb.nodesource.com/setup_14.x -o nodesource_setup.sh
RUN bash nodesource_setup.sh ; rm nodesource_setup.sh
RUN apt-get update ; apt-get install -y gcc g++ make nodejs
RUN npm i ; npm run webpack-prod
RUN gradle war --no-daemon

FROM tomcat:9.0.46-jdk16-openjdk
EXPOSE 8080
RUN mkdir /var/www
COPY --from=build /home/gradle/src/build/libs/*.war /var/www/jboard.war
USER root
RUN curl -sL https://deb.nodesource.com/setup_16.x -o nodesource_setup.sh
RUN bash nodesource_setup.sh ; rm nodesource_setup.sh
RUN apt-get update ; apt-get install -y gcc g++ make nodejs
COPY bin/ /usr/local/tomcat/node-bin/
COPY resources/docker/ROOT.xml /usr/local/tomcat/conf/Catalina/localhost/ROOT.xml
COPY resources/docker/package.json /usr/local/tomcat/node-bin/package.json
COPY resources/docker/scripts/useradd /bin/jboard-useradd
COPY resources/docker/scripts/userdel /bin/jboard-userdel
COPY resources/docker/scripts/userlist /bin/jboard-userlist
COPY resources/docker/scripts/change-user-password /bin/jboard-change-user-password
RUN chmod +x /bin/jboard-useradd ; chmod +x /bin/jboard-userdel ; chmod +x /bin/jboard-userlist ; chmod +x /bin/jboard-change-user-password
WORKDIR /usr/local/tomcat/node-bin
RUN npm i
WORKDIR /usr/local/tomcat
CMD ["catalina.sh", "run"]
