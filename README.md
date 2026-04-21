# Recipes Back

Backend de aplicación de recetas construido con Spring Boot.

## Tecnologías
- Java 17
- Spring Boot 3.2.0
- MySQL 8.0.33
- JWT para autenticación
- Flyway para migraciones de BD

## Requisitos previos
- Java 17
- Docker y Docker Compose

## Inicio rápido

### 1. Base de datos

Levanta MySQL con Docker:

```bash
docker compose up -d
```

Esto arranca un contenedor MySQL en el puerto `3306` con:
- Base de datos: `recipes_db`
- Usuario: `root`
- Contraseña: `root`

Para detenerlo:

```bash
docker compose down
```

Para detenerlo eliminando los datos:

```bash
docker compose down -v
```

### 2. Aplicación Java

```bash
mvn spring-boot:run
```

La aplicación estará disponible en `http://localhost:8080`.

## Endpoints principales

| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | /auth/register | Registrar usuario |
| POST | /auth/login | Obtener token JWT |
| GET | /recipes | Listar recetas |
| POST | /recipes | Crear receta |
| GET | /recipes/{id} | Obtener receta |
| PUT | /recipes/{id} | Actualizar receta |
| DELETE | /recipes/{id} | Eliminar receta |
| POST | /recipes/{id}/images | Añadir imagen |
| DELETE | /recipes/{id}/images/{imageId} | Eliminar imagen |

## Depuración desde dispositivo Android (USB)

La app Android usa `http://localhost:8080` como base URL. Para que el dispositivo conectado por USB enrute las peticiones a la máquina de desarrollo, necesitas ADB reverse.

### Instalación de ADB

```bash
sudo apt install adb
```

### Alias recomendado

Añade a tu `~/.bashrc`:

```bash
alias dev-android="adb reverse tcp:8080 tcp:8080 && echo ADB reverse activo"
```

```bash
source ~/.bashrc
```

### Uso

Conecta el dispositivo por USB, acepta el aviso de depuración USB en el móvil y ejecuta:

```bash
dev-android
```

> El reverse se pierde al desconectar el cable — repite el comando cada vez que lo vuelvas a conectar.

## Documentación OpenAPI

Con la aplicación corriendo, accede a:
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- Spec JSON: `http://localhost:8080/v3/api-docs`

Para descargar el spec:

```bash
curl http://localhost:8080/v3/api-docs -o openapi.json
```
