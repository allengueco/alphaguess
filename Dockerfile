FROM eclipse-temurin:21-jdk-alpine
RUN addgroup demogroup; adduser --ingroup demogroup --disabled-password demo
USER demo
LABEL authors="allengueco"

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw ./

RUN ./mvnw clean install

CMD ["./mvnw", "-pl", "backend", "spring-boot:run"]