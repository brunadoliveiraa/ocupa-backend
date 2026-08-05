# Estágio 1: Build da aplicação
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Executa o maven package ignorando testes para buildar o JAR mais rápido
RUN mvn clean package -DskipTests

# Estágio 2: Imagem final (mais leve, contendo apenas o JAR e o JRE)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Copia o JAR do estágio de build
COPY --from=build /app/target/*.jar app.jar
# A porta que o Render vai bater
EXPOSE 8080
# Comando para rodar
ENTRYPOINT ["java", "-jar", "app.jar"]
