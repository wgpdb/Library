FROM openjdk
RUN mkdir /app
COPY build/libs/library-0.0.1-SNAPSHOT.jar /app
WORKDIR /app
ENTRYPOINT ["java", "-jar", "library-0.0.1-SNAPSHOT.jar"]