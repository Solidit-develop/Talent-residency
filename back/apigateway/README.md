# API Gateway

Este proyecto es un API Gateway configurado para enrutar solicitudes a varios microservicios. La configuración incluye el uso de Docker y Docker Compose para levantar el gateway y los microservicios.

## Estructura del Proyecto

.
├── config.js
├── apigateway.js
├── .env
├── Dockerfile
├── docker-compose.yml
└── routes
├── index.js
└── registry.json

## Prerrequisitos

- [Docker](https://www.docker.com/)
- [Docker Compose](https://docs.docker.com/compose/)

## Paso 1: Configurar el Proyecto

### 1.1. Crear el Archivo `.env`

Crea un archivo `.env` en la raíz del proyecto con el siguiente contenido:

PORT=4000

### 1.2. Configurar `config.js`

Configura `config.js` para leer las variables de entorno usando `dotenv`:

```javascript
const dotenv = require("dotenv");
```

### 1.3. Configurar routes/registry.json

Asegúrate de que el archivo routes/registry.json contenga las rutas de los microservicios:

```json
{
  "services": {
    "users": {
      "apiName": "users",
      "host": "http://localhost",
      "port": "3001",
      "url": "http://localhost:3001/"
    },
    "payment": {
      "apiName": "payment",
      "host": "http://localhost",
      "port": "3002",
      "url": "http://localhost:3002/"
    }
  }
}
```

## Paso 2: Construir y Ejecutar con Docker

### 2.1. Crear el Dockerfile

Crea un archivo Dockerfile en la raíz del proyecto con el siguiente contenido:

```
# Usar una imagen base de Node.js
FROM node:22.2.0

# Argumentos de construcción
ARG PORT

# Establecer el directorio de trabajo en el contenedor
WORKDIR /usr/src/app

# Copiar el package.json y el package-lock.json
COPY package*.json ./

# Instalar las dependencias
RUN npm install

# Copiar el resto de la aplicación
COPY . .

# Configurar las variables de entorno
ENV PORT=$PORT

# Exponer el puerto
EXPOSE $PORT

# Comando para ejecutar la aplicación
CMD ["node", "apigateway.js"]
```

#### 2.1.1 Construir la Imagen en Docker Hub

```
docker build --build-arg PORT=4000 -t myusername/apigateway:latest .
```

### 2.1.2 Publicar la Imagen en Docker Hub

```
docker push myusername/apigateway:latest
```
