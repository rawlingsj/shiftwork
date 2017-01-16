FROM java:openjdk-8-jre-alpine

ENV JHIPSTER_SLEEP 0

# add directly the war
ADD *.war /app.war

RUN sh -c 'touch /app.war'
VOLUME /tmp
EXPOSE 8080
EXPOSE 5005
CMD echo "The application will start in ${JHIPSTER_SLEEP}s..." && \
    sleep ${JHIPSTER_SLEEP} && \
    java "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005" -Djava.security.egd=file:/dev/./urandom -DcreateDemoData=true -jar /app.war