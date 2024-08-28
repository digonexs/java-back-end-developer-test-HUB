# Usando uma imagem base do OpenJDK 17, pois o projeto utiliza Java 17
FROM openjdk:17-jdk-slim

# Define o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copia o arquivo de compilação do Maven para o contêiner
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

# Instala o Maven e compila o projeto, criando um arquivo JAR
RUN ./mvnw package -DskipTests

# Copia o arquivo JAR gerado para a pasta de execução do contêiner
COPY target/cupom-system-0.0.1-SNAPSHOT.jar app.jar

# Expõe a porta 8080, que é a porta padrão para aplicações Spring Boot
EXPOSE 8080

# Define o comando de execução da aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
