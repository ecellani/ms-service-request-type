FROM rodrigozc/jdk8-filebeat-consul:latest
MAINTAINER Sysmap Solutions <rodrigo.castilho@sysmap.com.br>

ENV FILEBEAT_NAME ms-service-request-type
ENV FILEBEAT_TAGS microservices
ENV FILEBEAT_HOSTS elk:5044
ENV FILEBEAT_INDEX poc

ADD service.json /opt/consul/config

ADD build/libs/*.jar /app/app.jar
ADD application.yml /app/application.yml
