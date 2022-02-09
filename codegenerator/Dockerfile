FROM openjdk
# FROM arm64v8/openjdk

COPY ./target/codegenerator-1.0.jar /var/app/codegenerator.jar
COPY ./templates /var/app/templates

EXPOSE 8080

WORKDIR /var/app

CMD ["java", "-jar", "/var/app/codegenerator.jar"]
