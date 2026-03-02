# 💳 Sistema de Gestión de Pagos con Tarjetas de Crédito

**Trabajo Final - DISEÑO DE BASES DE DATOS - 2025**

Sistema RESTful de gestión de pagos con tarjetas de crédito desarrollado con **Spring Boot 3**, **JPA/Hibernate** y **MySQL**.

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
- **Spring Data JPA**
- **Hibernate** 6.x
- **MySQL** 5.7+
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

- ✅ **MySQL 5.7 o superior** (en ejecución)
  ```bash
  mysql --version
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

### Paso 2: Configurar MySQL

La base de datos **se creará automáticamente** al iniciar la aplicación. Sin embargo, necesitas tener MySQL corriendo:

```bash
# Iniciar MySQL
brew services start mysql

# Verificar que está corriendo
mysql -u root -p
# (presiona Enter si no tienes contraseña)
```

### Paso 3: Verificar y actualizar credenciales (Opcional)

Si MySQL tiene contraseña, edita el archivo:

📄 **`src/main/resources/application.properties`**

```properties
# Línea 9-10: Cambiar si tu usuario/contraseña es diferente
spring.datasource.username=root        # Cambiar si es necesario
spring.datasource.password=tu_contraseña  # Tu contraseña MySQL
```

**Default (sin cambios):**
- Usuario: `carduser`
- Contraseña: `cardpass`

La BD `cardpurchases` **se crea automáticamente** ✅

### Paso 4: Compilar el proyecto

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

### Paso 5: Ejecutar la aplicación

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

# Levanta los contenedores (MySQL + App)
docker-compose up -d

# Espera 15-20 segundos para que la BD esté lista
sleep 20

# Verifica el estado
docker-compose ps
```

**Resultado esperado:**
```
NAME                  IMAGE               SERVICE   STATUS
cardpurchases-db-1    mysql:8.0           db        Up (healthy)
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

# Logs de MySQL
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

✅ **No necesitas instalar MySQL localmente**
✅ **Ambiente aislado y reproducible**
✅ **Fácil de compartir con otros desarrolladores**
✅ **No consumir recursos locales (MySQL detenido)**
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
│   │   ├── model/           # 11 entidades JPA
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
│   │   ├── repositories/    # 11 repositorios
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

## 🗄️ Base de Datos

### Creación Automática

✅ La base de datos **se crea automáticamente** al iniciar la aplicación:

1. **Verifica que MySQL esté corriendo** (o que Docker esté activo)
2. **Ejecuta:** `mvn spring-boot:run` o `docker-compose up -d`
3. **La BD `cardpurchases` se creará automáticamente** (junto con todas las tablas)

### Credenciales por defecto

```
Usuario: carduser
Contraseña: cardpass
Base de datos: cardpurchases
```

### Tablas creadas automáticamente

- banks
- cardholders
- cards
- purchases
- promotions
- quotas
- payments
- Tablas de relaciones (N:N)

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

### Ver queries SQL en consola

Ya está configurado por default:
```properties
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

### Cambiar nivel de logs

En `application.properties`:
```properties
logging.level.root=INFO
logging.level.org.springframework=DEBUG
logging.level.org.hibernate=DEBUG
```

---

## 🐛 Troubleshooting

| Problema | Solución |
|----------|----------|
| **`Connection refused` a MySQL** | Asegúrate que MySQL esté corriendo: `brew services start mysql` |
| **`Access denied for user 'carduser'`** | Verifica credenciales en `application.properties` |
| **`Port 8080 already in use`** | Cambia el puerto en `application.properties` o cierra el proceso que usa el puerto |
| **`Cannot find symbol BankController`** | Ejecuta: `mvn clean compile` |
| **Tests fallan** | Verifica que MySQL esté corriendo y las credenciales sean correctas |
| **Docker containers no inician** | Verifica que Docker esté corriendo: `docker-compose ps` |
| **BD vacía después de `docker-compose down`** | Usa `docker-compose down` (sin `-v`) para mantener los datos |

---

## 🚀 Fase 2: Implementacion con MongoDB
- Hacer checkout a la rama **main-mongodb** 