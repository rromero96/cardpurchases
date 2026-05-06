# Informe Técnico — Sistema de Gestión de Pagos con Tarjetas

**Trabajo Final — Diseño de Bases de Datos — MG IS 2025 — UNLP**

**Autor:** Rodrigo Romero

---

## 1. Decisiones de Mapeo — MySQL (rama `main`)

### 1.1 Relaciones Bidireccionales

Se utilizaron relaciones bidireccionales en los casos donde la navegación en ambos sentidos tiene valor funcional:

- **Bank ↔ Card** y **Bank ↔ Promotion**: el banco conoce sus tarjetas y promociones; cada tarjeta/promoción conoce a su banco. Esto permite agregar promociones a un banco y, desde la promoción, acceder al banco emisor.
- **Purchase ↔ Quota**: la compra en cuotas conoce sus cuotas (`@OneToMany(mappedBy="purchase")`); cada cuota conoce su compra (`@ManyToOne`). Necesario para generar y recuperar cuotas de una compra.
- **Purchase ↔ Promotion**: relación `@ManyToMany` bidireccional con tabla join `purchase_promotion`. Purchase es el lado owner; Promotion usa `mappedBy`. Permite conocer las promociones aplicadas a una compra y, al eliminar una promoción, identificar las compras afectadas.

### 1.2 Carga de Objetos bajo Demanda (Lazy)

Todas las relaciones `@ManyToOne` y `@OneToMany` usan `FetchType.LAZY`. Las colecciones no se cargan hasta ser accedidas explícitamente dentro de una transacción activa. Esto evita problemas de rendimiento al consultar entidades con múltiples colecciones (ej. un banco con cientos de tarjetas).

### 1.3 Operaciones en Cascada

| Relación | Cascade | orphanRemoval |
|---|---|---|
| Bank → Card | `ALL` | `true` |
| Bank → Promotion | `ALL` | `true` |
| Purchase → Quota | `ALL` | `true` |
| Purchase ↔ Promotion (join table) | Ninguno | — |

La join table `purchase_promotion` no tiene cascade para preservar la independencia de las promociones: una promoción puede ser eliminada sin eliminar las compras, pero se limpia la join table manualmente antes de borrar.

### 1.4 Herencia — SINGLE_TABLE

Tanto `Purchase` como `Promotion` usan `InheritanceType.SINGLE_TABLE` con discriminador:

- `Purchase` → `CashPayment` (`"CASH"`) y `MonthlyPayments` (`"MONTHLY"`)
- `Promotion` → `Discount` (`"DISCOUNT"`) y `Financing` (`"FINANCING"`)

Se eligió SINGLE_TABLE sobre JOINED para evitar JOINs en cada consulta. Las columnas específicas de cada subtipo quedan nullable para las filas de los demás (ej. `store_discount` es null para `MonthlyPayments`).

### 1.5 Embeber Objetos vs Referencias

No se usó `@Embedded`. Todas las entidades tienen identidad propia (`@Id` autoincremental) y ciclo de vida independiente. Esto garantiza normalización y permite que una misma Promotion sea referenciada por múltiples Purchases.

### 1.6 Uso de Transacciones

Todos los métodos de escritura del service layer están anotados con `@Transactional`. Esto garantiza atomicidad en operaciones compuestas, como la creación de una `MonthlyPayments` junto con sus N objetos `Quota`, o la limpieza de la join table antes de eliminar una `Promotion`.

---

## 2. Decisiones de Mapeo — MongoDB (rama `main-mongodb`)

### 2.1 Embeber Objetos vs Referencias (@DBRef)

Se optó por **referencias (`@DBRef`) en todas las relaciones** en lugar de embedding. Las entidades del dominio tienen identidad y ciclo de vida propios — embeber implicaría duplicar datos o perder la capacidad de consultar documentos individualmente.

| Relación | Decisión |
|---|---|
| Purchase → Card, CardHolder, Bank | `@DBRef` — entidades independientes con muchas referencias cruzadas |
| Purchase → Promotion | `@DBRef List<Promotion>` — una promoción puede aplicarse a múltiples compras |
| Purchase → Quota | `@DBRef List<Quota>` — las cuotas se guardan en colección propia para consultarlas por mes |
| Promotion → Bank | `@DBRef` — el banco existe independientemente |

Al eliminar una `Promotion`, se limpian manualmente todas las referencias `@DBRef` en los documentos `Purchase` afectados antes de borrar el documento, evitando referencias huérfanas.

### 2.2 Herencia en MongoDB

La herencia se maneja mediante el campo `_class` que Spring Data MongoDB agrega automáticamente al persistir. Al leer un documento, Spring Data instancia el subtipo correcto (`CashPayment`, `MonthlyPayments`, `Discount`, `Financing`).

Todas las subclases apuntan a la misma colección (`purchases` o `promotions`) mediante `@Document`.

### 2.3 Consultas Analíticas — Aggregation Pipelines

Las consultas que en MySQL se resuelven con SQL GROUP BY se implementan en MongoDB con Aggregation Pipelines via `MongoTemplate`, evitando traer colecciones completas a memoria:

| Caso de uso | Pipeline |
|---|---|
| Banco con más compras | `purchases → $lookup cards → $group by bank → $sort → $limit 1` |
| Clientes por banco | `cards → $match bank.$id → $group by cardholder.$id → $count` |
| Top 10 titulares por gasto | `purchases → $lookup cards → $group by cardholder → $sum finalAmount → $sort → $limit 10` |
| Local con más compras | `purchases → $group by store → $sort → $limit 1` |

### 2.4 Uso de Transacciones

Se usa `@Transactional` en los métodos de escritura. Sin un replica set configurado, MongoDB no ejecuta transacciones multi-documento reales; la anotación es declarativa. En un entorno de producción con replica set, el rollback automático funcionaría correctamente.

---

## 3. Cómo Levantar y Probar

Para instrucciones completas de configuración y ejecución ver el `README.md` del repositorio.

**Resumen:**
```bash
# Levantar con Docker Compose (MySQL en rama main, MongoDB en rama main-mongodb)
docker-compose up -d

# Ejecutar los 18 tests de integración
mvn test
```

Los tests cubren los 11 casos de uso funcionales + creación de ambos tipos de promociones (Discount y Financing). La colección Postman `CardPurchases.postman_collection.json` permite probar los endpoints manualmente.
