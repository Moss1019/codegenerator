FROM openjdk

COPY ./target/codegenerator-1.0.jar /var/app/codegenerator.jar
COPY ./templates /var/app/templates

WORKDIR /var/app/

CMD ["java", "-jar", "/var/app/codegenerator.jar"]
