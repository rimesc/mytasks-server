FROM openjdk:8-jre-alpine
ADD target/mytasks-server-*.jar app.jar
ADD application*.yml ./
ADD keystore.jks ./
ENV JAVA_OPTS="-Dapplication.home=/mytasks"
ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar
