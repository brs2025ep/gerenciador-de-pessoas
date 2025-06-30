# 1 : Construir fat jar
FROM gradle:8.14-jdk21-alpine AS build
WORKDIR /app
COPY ../../ .
RUN gradle clean build -x test

# 2: Imagem final para executar o fat jar
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]