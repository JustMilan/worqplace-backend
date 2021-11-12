FROM openjdk:17

WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod 777 mvw
RUN ./mvnw dependency:go-offline

COPY src ./src

CMD ["./mvnw", "spring-boot:run"]
