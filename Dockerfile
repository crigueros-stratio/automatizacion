#desplegar en dcos
#mvn clean package
#docker rmi camiloriguer/ingesta
#docker build -f Dockerfile -t camiloriguer/ingesta .
#docker push camiloriguer/ingesta

FROM openjdk

USER root

RUN mkdir -p /usr/apps/
WORKDIR /usr/apps/

ADD target/automatizacion-0.0.1-SNAPSHOT.jar app.jar
ADD ca-saas.crt ca-saas.crt
ADD entrypoint.sh entrypoint.sh

RUN chmod a+x ./entrypoint.sh
RUN chown $USER:$USER ./entrypoint.sh

ENTRYPOINT ["./entrypoint.sh"]


#ADD certs-node/ /user/apps/
#RUN keytool -importcert -keystore $JAVA_HOME/jre/lib/security/cacerts -alias ca -file /user/apps/certificado.der -storepass changeit -noprompt
#RUN keytool -importcert -keystore client-truststore.jks -alias ca -file /user/apps/node.pem -noprompt
#RUN keytool -importcert -keystore client-truststore.jks -alias ca -file /user/apps/node.key -noprompt
#pasar el der del node a cacerts de la instancia y pasar el key y pem a el trustore creado del cliente
#cambio from openjdk:8 