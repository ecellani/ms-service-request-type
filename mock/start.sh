docker run -it --rm -p 8080:8080 -v $(pwd)/webapps:/usr/local/tomcat/webapps/ --name poc-vivo tomcat:8.0-jre8-alpine
