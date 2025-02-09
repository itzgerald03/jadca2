# Use the official Tomcat base image
FROM tomcat:10.0-jdk11

# Copy the WAR file from the war folder to the Tomcat webapps directory
COPY ./war/JADCA2.war /usr/local/tomcat/webapps/ROOT.war

# Expose port 8080 for web traffic
EXPOSE 8080

# Start Tomcat server
CMD ["catalina.sh", "run"]
