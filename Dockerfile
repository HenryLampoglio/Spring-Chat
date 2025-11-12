FROM eclipse-temurin:17-jre-focal

# 3. Copia o .jar do estágio "builder"
WORKDIR /app

COPY target/*.jar app.jar
# 5. Define o ponto de entrada (ENTRYPOINT)
# Corre a aplicação usando o "loader" do Spring que sabe
# como usar as pastas "explodidas".
ENTRYPOINT ["java", "-jar", "app.jar"]