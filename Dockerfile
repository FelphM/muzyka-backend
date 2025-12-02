# --- Etapa 1: Construcción (Build) ---
# Usamos una imagen de Java 21 completa para compilar
FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /app

# Copiamos todo tu código (incluida la carpeta wallet)
COPY . .

# Damos permisos de ejecución al instalador de Gradle y construimos
RUN chmod +x gradlew
# El flag -x test salta los tests para que el despliegue sea más rápido y seguro por ahora
RUN ./gradlew build -x test --no-daemon

# --- Etapa 2: Ejecución (Run) ---
# Usamos una imagen más ligera solo para correr la app
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copiamos el JAR generado en la etapa anterior
COPY --from=build /app/build/libs/*.jar app.jar

# ¡IMPORTANTE! Copiamos la carpeta del wallet para que el JAR la encuentre
COPY --from=build /app/wallet ./wallet

# Le decimos a Render que arranque la app usando el puerto que ellos nos asignen ($PORT)
# CAMBIO: Quitamos los corchetes [] para permitir que $PORT se lea correctamente
CMD java -Dserver.port=$PORT -jar app.jar