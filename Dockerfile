# Usando uma imagem base que já inclui Maven e OpenJDK 17
FROM maven:3.8.6-openjdk-17-slim AS builder

# Define o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copia o arquivo de configuração do Maven (pom.xml)
COPY pom.xml .

# Baixar dependências do Maven para aproveitar o cache do Docker
RUN mvn dependency:go-offline -B

# Copia o código-fonte do aplicativo para o contêiner
COPY src src

# Compila o projeto, ignorando testes para acelerar o processo
RUN mvn package -DskipTests

# ---------------------------------------------------
# Segunda etapa: construir a imagem final mais enxuta
# ---------------------------------------------------

# Usando uma imagem base do OpenJDK 17 mais enxuta
FROM openjdk:17-jdk-slim

# Define o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copia o arquivo JAR gerado da etapa de build para a imagem final
COPY --from=builder /app/target/cupom-system-0.0.1-SNAPSHOT.jar app.jar

# Expõe a porta 8080, que é a porta padrão para aplicações Spring Boot
EXPOSE 8080

# Define o comando de execução da aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
