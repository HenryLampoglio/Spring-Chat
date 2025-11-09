# ----- Estágio 1: Build -----
# Usa a sua imagem base (maven + temurin) para compilar o projeto
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /build

# 1. Copia o pom.xml e baixa as dependências (para cache)
COPY pom.xml .
RUN mvn dependency:go-offline

# 2. Copia o código-fonte e compila o .jar
COPY src src
RUN mvn package -DskipTests

# ----- Estágio 2: Runtime -----
# Usa uma imagem JRE (Runtime) limpa e leve, da mesma família Temurin.
# É mais pequena porque não inclui o Maven nem ferramentas de compilação.
FROM eclipse-temurin:17-jre-focal
WORKDIR /app

# 3. Copia o .jar do estágio "builder"
COPY --from=builder /build/target/*.jar app.jar

# 4. A "Magia": Extrai o JAR em camadas
# Isto cria as pastas BOOT-INF/lib e BOOT-INF/classes
RUN java -Djarmode=tools -jar app.jar extract

# 5. Define o ponto de entrada (ENTRYPOINT)
# Corre a aplicação usando o "loader" do Spring que sabe
# como usar as pastas "explodidas".
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]