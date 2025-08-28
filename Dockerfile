# Usa uma imagem base que já tem o Maven e o JDK instalados
FROM maven:3.9.6-eclipse-temurin-17
WORKDIR /app

# Copia o arquivo de configuração do Maven para cache de dependências
COPY pom.xml .
RUN mvn dependency:go-offline

# Apenas define o ENTRYPOINT. O código-fonte será montado pelo Docker Compose.
ENTRYPOINT ["/bin/bash", "-c", "mvn clean spring-boot:run"]