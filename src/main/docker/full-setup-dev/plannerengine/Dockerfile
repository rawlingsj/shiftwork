FROM jboss/wildfly
ADD plannerengine-0.0.1-SNAPSHOT.war /opt/jboss/wildfly/standalone/deployments/
RUN /opt/jboss/wildfly/bin/add-user.sh admin Admin#70365 --silent
# Default wildfly debug port
EXPOSE 8787
EXPOSE 9990
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0", "--debug"]