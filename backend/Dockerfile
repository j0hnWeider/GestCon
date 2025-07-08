# Dockerfile para o backend Spring Boot
FROM openjdk:17-jdk-slim

# Definir diretório de trabalho
WORKDIR /app

# Copiar arquivo pom.xml e baixar dependências
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Dar permissão de execução ao mvnw
RUN chmod +x mvnw

# Baixar dependências (cache layer)
RUN ./mvnw dependency:go-offline -B

# Copiar código fonte
COPY src ./src

# Compilar aplicação
RUN ./mvnw clean package -DskipTests

# Expor porta da aplicação
EXPOSE 8080

# Comando para executar a aplicação
CMD ["java", "-jar", "target/gestcon-backend-1.0.0.jar"]
