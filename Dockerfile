FROM amazoncorretto:17.0.10-alpine

WORKDIR app

COPY target/FileSystem.jar .

ENTRYPOINT ["java","-jar", "FileSystem.jar"]