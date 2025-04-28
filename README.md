# 📊 Cache API

## Descripción

Esta API expone un servicio que **calcula la suma de dos números** y **aplica un porcentaje** obtenido de un **servicio externo**.  
Además:
- Guarda el porcentaje en **caché** usando **Caffeine**.
- Si el servicio externo falla, **recupera el porcentaje cacheado** para garantizar la operación.
- Registra el historial de llamadas en una base de datos **PostgreSQL**.

---

## 🛠️ Funcionalidades principales

- **Sumar** dos números.
- **Obtener** un porcentaje desde un servicio externo.
- **Guardar en caché** el porcentaje usando **Caffeine**.
- **Fallback automático**: recuperación del porcentaje cacheado si el servicio externo falla.
- **Guardar historial** de operaciones en una base de datos PostgreSQL.
- **Documentación OpenAPI/Swagger** integrada.

---

## 🚀 ¿Cómo correr la API localmente?

### 1. Prerrequisitos

- Java 21
- Maven 3.8 o superior
- Docker y Docker Compose instalados
- Git

### 2. Clonar el proyecto

```bash
git clone https://github.com/elkingrana2/api-cache.git
```

### 3. Levantar los servicios con Docker Compose
Este proyecto incluye un docker-compose.yml que levanta:
- API de Cache
- Base de datos PostgreSQL
```bash
docker-compose up --build
```

### 4. Acceder a la documentación Swagger
Una vez levantado el proyecto, puedes acceder a la documentación interactiva de la API en:
http://localhost:8080/swagger-ui.html

---

## 🛠️ Tecnologías utilizadas
- Java 21``
- Spring Boot 3.5.0-SNAPSHOT
- Spring Web
- Spring Data JPA
- Spring Boot Starter Cache
- Caffeine Cache
- Hibernate 6.3.1.Final
- PostgreSQL (Base de datos)
- Docker y Docker Compose
- Swagger/OpenAPI (vía springdoc-openapi-starter-webmvc-ui 2.2.0)
- Swagger-annotations 2.2.29
- Awaitility para pruebas asíncronas
