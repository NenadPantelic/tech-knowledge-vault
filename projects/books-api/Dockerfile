FROM eclipse-temurin:25-jdk AS build

WORKDIR /workspace

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
COPY src/ src/

RUN sh ./mvnw -B package -DskipTests

FROM eclipse-temurin:25-jre

WORKDIR /app

RUN useradd --system --uid 10001 --no-create-home appuser
COPY --from=build /workspace/target/books-api-0.0.1-SNAPSHOT.jar app.jar

USER 10001
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
