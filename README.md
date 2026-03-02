# 💳 Sistema de Gestión de Pagos con Tarjetas de Crédito

**Trabajo Final - DISEÑO DE BASES DE DATOS - 2025**

Sistema RESTful de gestión de pagos con tarjetas de crédito desarrollado con **Spring Boot 3**, **Spring Data MongoDB** y **MongoDB**.

> **FASE 2 COMPLETADA**: Migración de MySQL a MongoDB (2 Marzo 2026)

---

## 📋 Descripción del Proyecto

Sistema integral para la gestión de:
- 🏦 **Bancos** y sus clientes (titulares de tarjetas)
- 💳 **Tarjetas de crédito** con validación de emisión y vencimiento
- 🛒 **Compras** (en un solo pago o en cuotas)
- 🎁 **Promociones** (descuentos y financiaciones especiales)
- 💰 **Generación automática de pagos mensuales** con cálculo de totales

---

## 🛠️ Tecnologías Utilizadas

- **Java** 17+
- **Spring Boot** 3.0
- **Spring Data MongoDB**
- **MongoDB** 7.0+
- **Maven** como gestor de dependencias
- **JUnit 5** para testing
- **Docker** & **Docker Compose** (opcional)

---

## 📦 Requisitos Previos

Elige la opción que prefieras:

### Opción A: Instalación Local (sin Docker)

- ✅ **Java 17 o superior**
  ```bash
  java -version
  ```

- ✅ **Maven 3.8 o superior**
  ```bash
  mvn -version
  ```

- ✅ **MongoDB 7.0 o superior** (en ejecución)
  ```bash
  mongosh --version
  ```

### Opción B: Ejecución con Docker

- ✅ **Docker** instalado
  ```bash
  docker --version
  ```

- ✅ **Docker Compose** instalado
  ```bash
  docker-compose --version
  ```

---

## 🚀 Opción A: Instalación Local (Sin Docker)

### Paso 1: Descargar o clonar el proyecto

```bash
# Opción A: Clonar desde Git
git clone https://github.com/rromero96/cardpurchases.git
cd cardpurchases

# Opción B: Descargar ZIP y extraer
# Luego navega a la carpeta del proyecto
cd cardpurchases
```

### Paso 2: Instalar y ejecutar MongoDB

```bash
# Instalar MongoDB (si no está instalado)
brew tap mongodb/brew
brew install mongodb-community

# Iniciar MongoDB
brew services start mongodb-community

# Verificar que está corriendo
mongosh
# (presiona Exit para salir)
```

### Paso 3: Compilar el proyecto

```bash
# Navega a la carpeta del proyecto
cd cardpurchases

# Limpia y compila
mvn clean compile
```

**Resultado esperado:**
```
[INFO] BUILD SUCCESS
```

### Paso 4: Ejecutar la aplicación

```bash
mvn spring-boot:run
```

**Espera a ver este mensaje:**
```
... Tomcat started on port(s): 8080
... Application started successfully
```

**La aplicación está lista en:** 🔗 http://localhost:8080

---

## 🐳 Opción B: Ejecución con Docker

### Paso 1: Instalar Docker (Si no está instalado)

```bash
brew install colima docker docker-compose

# Iniciar Colima (motor de Docker en macOS)
colima start
```

### Paso 2: Levantar los servicios con Docker Compose

```bash
# Navega a la carpeta del proyecto
cd cardpurchases

# Levanta los contenedores (MongoDB + App)
docker-compose up -d

# Espera 15-20 segundos para que la BD esté lista
sleep 20

# Verifica el estado
docker-compose ps
```

**Resultado esperado:**
```
NAME                  IMAGE               SERVICE   STATUS
cardpurchases-db-1    mongo:7.0           db        Up (healthy)
cardpurchases-app-1   cardpurchases-app   app       Up
```

### Paso 3: Probar la aplicación

La aplicación estará disponible en: 🔗 http://localhost:8080

```bash
# Hacer un test rápido
curl http://localhost:8080/api/banks
```

### Ver logs en tiempo real

```bash
# Logs de la aplicación
docker-compose logs -f app

# Logs de MongoDB
docker-compose logs -f db

# Detener seguimiento: Ctrl+C
```

### Detener los servicios

```bash
# Detener sin eliminar volúmenes
docker-compose stop

# Reiniciar
docker-compose start

# Detener y eliminar todo (incluyendo BD)
docker-compose down

# Detener y eliminar incluyendo volúmenes de datos
docker-compose down -v
```

### Ventajas de usar Docker

✅ **No necesitas instalar MongoDB localmente**
✅ **Ambiente aislado y reproducible**
✅ **Fácil de compartir con otros desarrolladores**
✅ **No consumir recursos locales**
✅ **Compatible con CI/CD pipelines**

---

## 📝 Cómo Probar los Endpoints

### Opción 1: Postman (Recomendado)

1. **Importar la colección:**
   - Abre Postman
   - Click en **"Import"**
   - Selecciona: `CardPurchases.postman_collection.json`
   - ¡Listo! Tienes todos los 11 casos de uso

2. **Ejecutar en orden:**
   - SETUP (crear banco, titular, tarjeta)
   - CASO 1 a CASO 11 (todos los casos de uso)

### Opción 2: Ejecutar Tests Automáticos

```bash
# Ejecutar todos los tests
mvn clean test -Dtest=CardpurchasesApplicationTests

# Resultado esperado:
# Tests run: 17, Failures: 0, Errors: 0 ✅
```

---

## 📊 11 Casos de Uso Implementados

| # | Caso de Uso | Endpoint |
|---|------------|----------|
| 1 | Agregar promoción de descuento | `POST /api/promotions/discount` |
| 2 | Editar fechas de pago | `PUT /api/payments/{code}/due-dates` |
| 3 | Generar total de pago del mes | `GET /api/payments/month/{year}/{month}` |
| 4 | Obtener tarjetas >5 años | `GET /api/cards/old` |
| 5 | Obtener información de compra | `GET /api/purchases/{purchaseId}` |
| 6 | Eliminar promoción por código | `DELETE /api/promotions/{code}` |
| 7 | Promociones disponibles | `GET /api/promotions/available` |
| 8 | Top 10 titulares por gasto | `GET /api/cardholders/top-10-spending` |
| 9 | Local con más compras | `GET /api/purchases/store-most-purchases` |
| 10 | Banco con más compras | `GET /api/banks/most-purchases` |
| 11 | Clientes por banco | `GET /api/banks/client-count` |

---

## 📁 Estructura del Proyecto

```
cardpurchases/
├── src/main/
│   ├── java/com/tpdbd/cardpurchases/
│   │   ├── model/           # 11 entidades MongoDB
│   │   │   ├── Bank.java
│   │   │   ├── CardHolder.java
│   │   │   ├── Card.java
│   │   │   ├── Purchase.java (abstract)
│   │   │   ├── CashPayment.java
│   │   │   ├── MonthlyPayments.java
│   │   │   ├── Promotion.java (abstract)
│   │   │   ├── Discount.java
│   │   │   ├── Financing.java
│   │   │   ├── Quota.java
│   │   │   └── Payment.java
│   │   ├── repositories/    # 11 repositorios MongoDB
│   │   ├── services/        # 6 servicios
│   │   ├── controllers/     # 6 controladores (16 endpoints)
│   │   └── CardpurchasesApplication.java
│   └── resources/
│       └── application.properties
├── src/test/
│   └── java/CardpurchasesApplicationTests.java (17 tests ✅)
├── pom.xml
├── .gitignore
├── Dockerfile
├── docker-compose.yml
├── README.md (este archivo)
└── CardPurchases.postman_collection.json
```

---

## 🔌 API Endpoints Principales

### Banks (3 endpoints requeridos)
- `POST /api/banks` - Crear banco
- `GET /api/banks/most-purchases` - Banco con más compras
- `GET /api/banks/client-count` - Clientes por banco

### CardHolders (1 endpoint requerido)
- `GET /api/cardholders/top-10-spending` - Top 10 por gasto

### Cards (1 endpoint requerido)
- `GET /api/cards/old` - Tarjetas >5 años

### Purchases (2 endpoints requeridos)
- `GET /api/purchases/{id}` - Detalles de compra
- `GET /api/purchases/store-most-purchases` - Local con más compras

### Promotions (3 endpoints requeridos)
- `POST /api/promotions/discount` - Crear promoción
- `DELETE /api/promotions/{code}` - Eliminar promoción
- `GET /api/promotions/available` - Promociones disponibles

### Payments (2 endpoints requeridos)
- `PUT /api/payments/{code}/due-dates` - Editar fechas
- `GET /api/payments/month/{year}/{month}` - Total del mes

---

## 🗄️ Base de Datos (MongoDB)

### Creación Automática

✅ La base de datos **se crea automáticamente** al iniciar la aplicación:

1. **Verifica que MongoDB esté corriendo** (o que Docker esté activo)
2. **Ejecuta:** `mvn spring-boot:run` o `docker-compose up -d`
3. **La BD `cardpurchases` se creará automáticamente** (junto con todas las colecciones)

### Configuración por defecto

```
URI: mongodb://localhost:27017/cardpurchases
Base de datos: cardpurchases
```

### Colecciones creadas automáticamente

- banks
- cardholders
- cards
- purchases
- promotions
- quotas
- payments

---

## 🧪 Testing

### Ejecutar todos los tests

```bash
mvn clean test
```

### Ejecutar solo tests de integración

```bash
mvn test -Dtest=CardpurchasesApplicationTests
```

### Resultado esperado

```
Tests run: 17, Failures: 0, Errors: 0, Skipped: 0

✅ BUILD SUCCESS
```

### Cobertura de tests

Los 17 tests validan:
- ✅ 11 casos de uso requeridos
- ✅ 6 tests de validación de estructura

---

## ⚙️ Configuración Avanzada

### Cambiar puerto

Edita `application.properties`:
```properties
server.port=8081  # Puerto personalizado
```

### Cambiar URI de MongoDB

Edita `application.properties`:
```properties
spring.data.mongodb.uri=mongodb://user:pass@host:27017/dbname
```

### Ver logs de MongoDB

Ya está configurado por default:
```properties
logging.level.org.springframework.data.mongodb=DEBUG
```

---

## 🐛 Troubleshooting

| Problema | Solución |
|----------|----------|
| **`Connection refused` a MongoDB** | Asegúrate que MongoDB esté corriendo: `brew services start mongodb-community` |
| **`Port 8080 already in use`** | Cambia el puerto en `application.properties` o cierra el proceso que usa el puerto |
| **`Cannot find symbol BankController`** | Ejecuta: `mvn clean compile` |
| **Tests fallan** | Verifica que MongoDB esté corriendo y accesible en `mongodb://localhost:27017` |
| **Docker containers no inician** | Verifica que Docker esté corriendo: `docker-compose ps` |
| **BD vacía después de `docker-compose down`** | Usa `docker-compose down` (sin `-v`) para mantener los datos |

