
# Proyecto recipes-back

## Descripción
recipes-back es el backend de una aplicación de recetas construida con Spring Boot y MySQL, siguiendo la Arquitectura de Recetas v4.

## Tecnologías
- Java 17
- Spring Boot 3.0.x
- MySQL 8.0.33
- OpenAPI con SpringDoc

## Estructura del Proyecto
- `src/main/java/com/recipes/controllers`: Controladores REST
- `src/main/java/com/recipes/services`: Servicios con lógica de negocio
- `src/main/java/com/recipes/repositories`: Repositorios para acceso a datos
- `src/main/java/com/recipes/models`: Entidades y DTOs
- `src/main/resources/application.properties`: Configuración de la aplicación
- `src/test/java/com/recipes`: Tests unitarios y de integración

## Entidades
- `Recipe`: Representa una receta
- `RecipeImage`: Representa una imagen asociada a una receta
- `Category`: Representa una categoría de recetas
- `User`: Representa un usuario

## Endpoints
- `GET /recipes`: Obtiene todas las recetas
- `GET /recipes/{id}`: Obtiene una receta por ID
- `POST /recipes`: Crea una nueva receta
- `PUT /recipes/{id}`: Actualiza una receta existente
- `DELETE /recipes/{id}`: Elimina una receta
- `POST /recipes/{id}/images`: Añade imágenes a una receta
- `DELETE /recipes/{id}/images/{imageId}`: Elimina una imagen de una receta
- `GET /recipes/{id}/images/{imageId}`: Obtiene una imagen específica de una receta
- `POST /auth/register`: Registra un nuevo usuario
- `POST /auth/login`: Autentica un usuario y devuelve un token JWT

## Configuración de la Base de Datos
- Crear una base de datos MySQL llamada `recipes_db`
- Configurar la conexión en `application.properties`:
  ```
  spring.datasource.url=jdbc:mysql://localhost:3306/recipes_db
  spring.datasource.username=your_username
  spring.datasource.password=your_password
  ```

## Tareas
- [ ] Configurar el proyecto con Spring Boot y las dependencias necesarias
- [ ] Crear las entidades y repositorios
- [ ] Implementar los servicios con la lógica de negocio
- [ ] Desarrollar los controladores REST
- [ ] Configurar la seguridad con Spring Security y JWT
- [ ] Implementar la subida y almacenamiento de imágenes
- [ ] Escribir tests unitarios y de integración
- [ ] Generar la documentación OpenAPI
- [ ] Desplegar la aplicación

## Comandos
- `./mvnw spring-boot:run`: Inicia la aplicación
- `./mvnw test`: Ejecuta los tests
- `./mvnw package`: Genera el JAR ejecutable
