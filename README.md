# Tiny - Bank

Código de ejemplo de una API REST que simula un pequeño banco, construido siguiendo
**arquitectura hexagonal** (puertos y adaptadores) con **Spring Boot** y **MongoDB**.

## Funcionalidades

- Registro de usuario
- Creación de cuenta (wallet)
- Depósito de dinero en una cuenta
- Visualización de una cuenta (wallet): balance y movimientos
- Transferencia de dinero de una cuenta A a una cuenta B

## Stack tecnológico

| Tecnología | Uso |
|------------|-----|
| Java 11 | Lenguaje |
| Spring Boot 2.5.4 | Framework de aplicación (Web, Validation, Data MongoDB) |
| Gradle (wrapper 6.8.3) | Gestor de dependencias y build |
| MongoDB | Persistencia (embebida en `dev`, externa en `local`) |
| MapStruct 1.4.2 | Mapeo entre DTOs y modelos de dominio |
| Lombok | Reducción de código boilerplate |
| Springfox 3.0.0 | Documentación OpenAPI / Swagger UI |
| Spring Security (core) | Dependencia base de seguridad |

## Getting Started

### Requisitos previos

- **JDK 11** o superior instalado y disponible en el `PATH` (`java -version`).
- No necesitas instalar Gradle: el proyecto incluye el **Gradle Wrapper** (`./gradlew`).
- (Opcional) Una instancia de **MongoDB** solo si vas a usar el perfil `local`.
  Con el perfil por defecto `dev` se usa una base de datos Mongo **embebida**, así que
  no hace falta ninguna instalación adicional.

### Instalación / compilación

Este proyecto usa **Gradle** como gestor de paquetes y build. Clona el repositorio y
compila con el wrapper incluido:

```bash
git clone https://github.com/joguimar/tiny-bank.git
cd tiny-bank

# Linux / macOS
./gradlew build

# Windows
gradlew.bat build
```

Esto descargará las dependencias declaradas en `build.gradle`, compilará el código y
ejecutará los tests.

### Ejecución

Arranca la aplicación con el perfil por defecto (`dev`, Mongo embebida):

```bash
# Linux / macOS
./gradlew bootRun

# Windows
gradlew.bat bootRun
```

La API quedará disponible en el host y puerto configurados para la aplicación. En los
ejemplos siguientes se usa `<BASE_URL>` como marcador de posición de esa dirección base
(protocolo, host y puerto), que debes sustituir por la de tu entorno.

Para usar el perfil `local` (conecta a una MongoDB externa según `application-local.yml`):

```bash
./gradlew bootRun --args='--spring.profiles.active=local'
```

### Ejecución de tests

```bash
./gradlew test
```

## Endpoints de la API

| Método | Ruta | Descripción |
|--------|------|-------------|
| `POST` | `/user` | Registra un nuevo usuario (nombre y contraseña) |
| `POST` | `/wallet` | Crea una nueva wallet asociada a uno o varios usuarios |
| `PUT`  | `/wallet/{id}/money` | Deposita dinero en la wallet indicada |
| `GET`  | `/wallet/{id}` | Muestra el balance y los movimientos de una wallet |
| `POST` | `/wallet/money-tranfer` | Transfiere dinero entre dos wallets |

### Ejemplos de uso (curl)

```bash
# Crear un usuario
curl -X POST <BASE_URL>/user \
  -H "Content-Type: application/json" \
  -d '{"name": "alice", "password": "secret"}'

# Crear una wallet para un usuario
curl -X POST <BASE_URL>/wallet \
  -H "Content-Type: application/json" \
  -d '{"userIds": ["<userId>"]}'

# Depositar dinero
curl -X PUT <BASE_URL>/wallet/<walletId>/money \
  -H "Content-Type: application/json" \
  -d '{"amount": 100.0}'

# Consultar una wallet
curl <BASE_URL>/wallet/<walletId>

# Transferir dinero entre wallets
curl -X POST <BASE_URL>/wallet/money-tranfer \
  -H "Content-Type: application/json" \
  -d '{"amount": 50.0, "walletSource": "<walletId>", "walletTarget": "<walletId>"}'
```

## Documentación (Swagger)

Con la aplicación en marcha, la documentación interactiva OpenAPI está disponible en la
ruta `/swagger-ui/` sobre la dirección base de tu entorno:

```
<BASE_URL>/swagger-ui/
```

## Estructura del proyecto

El código sigue una **arquitectura hexagonal**, separando el dominio de los detalles de
infraestructura mediante puertos e interfaces:

```
src/main/java/es/jguimar/tinybankAPI
├── domain/model            # Modelos de dominio (User, Wallet)
├── application
│   ├── port/inbound        # Puertos de entrada (interfaces web, lectura)
│   ├── port/outbound       # Puertos de salida (interfaces de escritura/repositorio)
│   ├── service             # Servicios de aplicación
│   └── usecase             # Casos de uso (crear usuario/wallet, depositar, transferir, ver)
├── adapter
│   ├── rest                # Controladores REST + DTOs + mappers (MapStruct)
│   └── mongo               # Adaptadores de persistencia MongoDB
└── infrastructure
    ├── configuration       # Configuración (Swagger, Mongo embebida)
    └── exception           # Excepciones de dominio/infraestructura
```

## Perfiles de configuración

- **`dev`** (por defecto): usa una base de datos MongoDB **embebida**. No requiere
  instalación de MongoDB.
- **`local`**: usa las propiedades de `application-local.yml` para conectarse a una
  instancia de MongoDB **externa**.

## Notas y posibles mejoras

- La contraseña del usuario se almacena tal cual; en un entorno real debería **cifrarse**
  (p. ej. con `BCrypt`), y falta implementar autenticación/autorización aunque
  `spring-security-core` está entre las dependencias.
- Hay algunos typos históricos en rutas y paquetes (`money-tranfer`, paquete `tranform`)
  que se mantienen por compatibilidad con el código existente.
