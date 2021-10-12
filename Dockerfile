FROM openjdk:11
VOLUME /tmp
RUN mkdir /servicio-usb-base-logs/archived/ -p
VOLUME /servicio-usb-base-logs/archived/
ADD ./target/servicio-usb-base.jar servicio-usb-base.jar
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar servicio-usb-base.jar"]