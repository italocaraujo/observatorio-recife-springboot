#  imagem do OpenJDK 23
FROM eclipse-temurin:23-jdk AS build

# Instalar o Maven manualmente // Não consegui pular a linha 
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

# Definir o diretório de trabalho
WORKDIR /app

# Copiar o pom.xml e baixar dependências sem construir o código
COPY pom.xml ./
RUN mvn dependency:go-offline -B

# Copiar o restante do código do projeto
COPY src ./src

# Construir o aplicativo
RUN mvn clean package -DskipTests

# imagem JRE para 
FROM eclipse-temurin:23-jre

# Configurar o diretório de trabalho
WORKDIR /app

# Copiar o artefato do build
COPY --from=build /app/target/observatorio-economico-0.0.1-SNAPSHOT.jar app.jar

# Expor a porta padrão do Spring Boot
EXPOSE 8080

# Configurar opções da JVM
ENV JAVA_OPTS="-Xms512m -Xmx1024m"

# Executar o comando com opções de memória
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
