FROM incodehq/tomcat
RUN rm -rf ${DEPLOYMENT_DIR}/ROOT
COPY ${docker-plugin.resource.include} ${DEPLOYMENT_DIR}/ROOT.war
ADD entrypoint.sh /
RUN chmod 755 /entrypoint.sh
CMD /entrypoint.sh
EXPOSE 8080
