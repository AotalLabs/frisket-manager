FROM openjdk:8-jre

COPY build/libs/app.jar app.jar

CMD java \
    -Dspring.boot.admin.client.management-url=http://`curl http://rancher-metadata/2015-12-19/self/container/primary_ip`:8081 \
    -Dspring.boot.admin.client.health-url=http://`curl http://rancher-metadata/2015-12-19/self/container/primary_ip`:8081/health \
    -Dspring.boot.admin.client.service-url=http://`curl http://rancher-metadata/2015-12-19/self/container/primary_ip`:8080 \
    -Xmx512m \
    -Xms512m \
    -jar \
     /app.jar
