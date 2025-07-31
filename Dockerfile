# Usamos imagen oficial de OpenJDK 17
FROM eclipse-temurin:17-jdk-alpine

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos el archivo JAR generado al contenedor
COPY target/cuentas_movimientos-0.0.1-SNAPSHOT.jar app.jar

# Puerto que exponemos (debe coincidir con el puerto de la app)
EXPOSE 8082

# Comando para ejecutar la app
ENTRYPOINT ["java","-jar","app.jar"]