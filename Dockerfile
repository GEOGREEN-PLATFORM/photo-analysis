FROM maven:3.8.4-openjdk-17 as builder
WORKDIR /app
COPY . /app/.
RUN mvn -f /app/pom.xml clean package -Dmaven.test.skip=true

# Используем более полный образ для runtime
FROM eclipse-temurin:17-jre
WORKDIR /app

# Убедитесь, что все необходимые библиотеки установлены
RUN apt-get update && apt-get install -y \
    libstdc++6 \
    libc6 && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Копируем JAR-файл
COPY --from=builder /app/target/*.jar /app/app.jar

# Копируем ресурсы (включая модель)
COPY ./src/main/resources /app/resources

EXPOSE 8181
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
