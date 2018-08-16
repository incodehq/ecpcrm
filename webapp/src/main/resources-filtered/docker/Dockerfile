FROM incodehq/tomcat:1.7.2
ADD ${docker-plugin.resource.include} ${DEPLOYMENT_DIR}/ROOT.war
ADD entrypoint.sh /
RUN chmod 755 /entrypoint.sh
CMD /entrypoint.sh
EXPOSE 8080
