# BE Product Otomotif — Project Backend

REST API untuk mengelola produk otomotif (motor/mobil) beserta varian, harga, dan stok — dibuat sebagai project backend.

## Tech Stack

| Komponen | Teknologi |
|---|---|
| Bahasa | Java 17 |
| Framework | Spring Boot 3.5.6 |
| Data Access | Spring Data JPA (Hibernate) |
| Database | H2 Database (File-based) |
| Build Tool | Maven |
| Validasi | Jakarta Bean Validation (`@Valid`) |
| Boilerplate | Lombok (`@Data`, `@RequiredArgsConstructor`, dll) |
| Version Control | Git |

## Struktur Project

```
com.mfathullah.be_product_otomotif
├── BeProductOtomotifApplication.java
├── model/                  # Entity JPA
│   ├── GroupObject.java
│   ├── ObjectType.java
│   ├── Brand.java
│   ├── Model.java
│   ├── Item.java
│   ├── Variant.java
│   └── Stock.java
├── model/dto/
│   ├── request/             # DTO untuk request body
│   └── response/            # DTO untuk response body
├── repository/              # Spring Data JPA repository
├── service/                 # Business logic
├── controller/              # REST controller
└── exception/               # Custom exception + global handler
```

## Desain Skema Data

```
GroupObject (1) ── (N) ObjectType
Brand       (1) ── (N) Model

GroupObject, ObjectType, Brand, Model ── (dipakai oleh) ── Item
Item        (1) ── (N) Variant
Variant     (1) ── (1) Stock
```

## Cara Menjalankan

### 1. Clone & masuk ke folder project
```bash
git clone <repository-url>
cd be-product-otomotif/be-product-otomotif
```
Kemudian buka project menggunakan IntelliJ IDEA (disarankan) dan pastikan menggunakan JDK 17.

### 2. Konfigurasi `application.properties`
```properties
# H2 Database
spring.datasource.url=jdbc:h2:file:./data/warehouse
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=mf_oto
spring.datasource.password=mf_oto

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

server.port=8080 #default
```

### 3. Jalankan aplikasi
```bash
Java Version : 17
Build Tool   : Maven
./mvnw spring-boot:run
```

### 4. Cek H2 Console (opsional, untuk lihat isi database langsung)
Buka `http://localhost:8080/h2-console`, gunakan JDBC URL sesuai `application.properties` di atas.
Gunakan konfigurasi berikut:
```
JDBC URL: jdbc:h2:file:./data/warehouse
username: mf_oto
password: mf_oto
```
  
Database akan otomatis tersimpan pada:
```
be-product-otomotif/
└── data/
    └── warehouse.mv.db
```

### 5. Import Postman Collection and Swagger
File Collection bisa langsung di-import ke Postman untuk testing.
`MF-OTO.postman_collection.json`
→ Collection utama untuk Product, Variant, Stock, dan Purchase.

`MF-OTO-PARAMS.postman_collection.json`
→ Collection untuk master data (Group Object, Brand, Model, Object Type).

API juga dapat diuji melalui Swagger UI: `http://localhost:8080/swagger-ui/index.html`

## Features

```
- CRUD Group Object
- CRUD Brand
- CRUD Object Type
- CRUD Model
- CRUD Item
- CRUD Variant
- Stock Management
- Purchase Transaction
- Optimistic Locking
- Pessimistic Locking
- Global Exception Handling
- Swagger/OpenAPI Documentation
- H2 Database
```

## Architecture

```
Controller
      │
      ▼
Service
      │
      ▼
Repository
      │
      ▼
H2 Database
```

## Requirements

```
- Java 17+
- Maven 3.9+
- IntelliJ IDEA (recommended)
```

## Urutan Penggunaan API

Karena ada relasi berjenjang, data **wajib dibuat berurutan** agar tidak kena error 404 `ResourceNotFoundException`:

```
1. POST /api/group-objects      → buat kategori induk (mis. "Kendaraan Bermotor")
2. POST /api/brands              → buat merek (mis. "Honda"), independen, bisa kapan saja
3. POST /api/object-types        → butuh groupObjectId dari langkah 1
4. POST /api/models              → butuh brandId dari langkah 2
5. POST /api/items               → butuh groupObjectId, objectTypeId, brandId, modelId
6. POST /api/variants            → butuh itemId dari langkah 5, otomatis membuat Stock awal
7. GET  /api/items/{id}/detail   → lihat Item lengkap + semua Variant + status Stock
8. POST /api/purchases           → simulasi transaksi pembelian (uji logic anti-oversell)
```

### Contoh alur cURL end-to-end

```bash
# 1. GroupObject
curl -X POST "http://localhost:8080/api/group-objects" \
  -H "Content-Type: application/json" \
  -d '{ "name": "Kendaraan Bermotor" }'
# response: { "id": 1, "name": "Kendaraan Bermotor", ... }

# 2. Brand
curl -X POST "http://localhost:8080/api/brands" \
  -H "Content-Type: application/json" \
  -d '{ "name": "Honda" }'
# response: { "id": 1, "name": "Honda", ... }

# 3. ObjectType (pakai groupObjectId = 1)
curl -X POST "http://localhost:8080/api/object-types" \
  -H "Content-Type: application/json" \
  -d '{ "name": "Motor", "groupObjectId": 1 }'
# response: { "id": 1, "name": "Motor", ... }

# 4. Model (pakai brandId = 1)
curl -X POST "http://localhost:8080/api/models" \
  -H "Content-Type: application/json" \
  -d '{ "name": "Beat", "brandId": 1 }'
# response: { "id": 1, "name": "Beat", ... }

# 5. Item (pakai id dari langkah 1, 2, 3, 4)
curl -X POST "http://localhost:8080/api/items" \
  -H "Content-Type: application/json" \
  -d '{
    "code": "HONDA-BEAT",
    "name": "Honda Beat",
    "groupObjectId": 1,
    "objectTypeId": 1,
    "brandId": 1,
    "modelId": 1
  }'
# response: { "id": 1, "code": "HONDA-BEAT", ... }

# 6. Variant (pakai itemId = 1), otomatis buat Stock awal 20 unit
curl -X POST "http://localhost:8080/api/variants" \
  -H "Content-Type: application/json" \
  -d '{
    "itemId": 1,
    "sku": "HONDA-BEAT-CBS-RED",
    "name": "Beat CBS-ISS Merah",
    "color": "Merah",
    "engineCc": 110,
    "price": 18500000,
    "initialStock": 20
  }'
# response: { "id": 1, "sku": "HONDA-BEAT-CBS-RED", ... }

# 7. Lihat detail Item lengkap
curl -X GET "http://localhost:8080/api/items/1/detail"

# 8. Simulasi pembelian
curl -X POST "http://localhost:8080/api/purchases" \
  -H "Content-Type: application/json" \
  -d '{
    "customerName": "Budi Santoso",
    "lines": [ { "variantId": 1, "quantity": 2 } ]
  }'
```

## Daftar Endpoint

### Master Data

| Method | Endpoint | Keterangan |
|---|---|---|
| GET | `/api/group-objects` | List semua GroupObject aktif |
| GET | `/api/group-objects/{id}` | Detail GroupObject |
| POST | `/api/group-objects` | Buat GroupObject |
| PUT | `/api/group-objects/{id}` | Update GroupObject |
| DELETE | `/api/group-objects/{id}` | Soft delete GroupObject |
| GET | `/api/object-types?groupObjectId=` | List ObjectType, filter opsional |
| GET/POST/PUT/DELETE | `/api/object-types/...` | CRUD ObjectType |
| GET/POST/PUT/DELETE | `/api/brands/...` | CRUD Brand |
| GET | `/api/models?brandId=` | List Model, filter opsional |
| GET/POST/PUT/DELETE | `/api/models/...` | CRUD Model |

### Item

| Method | Endpoint | Keterangan |
|---|---|---|
| GET | `/api/items?page=&size=` | List Item (pagination) |
| GET | `/api/items/{id}` | Detail Item |
| GET | `/api/items/{id}/detail` | Item + semua Variant + status Stock |
| POST | `/api/items` | Buat Item |
| PUT | `/api/items/{id}` | Update Item |
| DELETE | `/api/items/{id}` | Soft delete Item |

### Variant

| Method | Endpoint | Keterangan |
|---|---|---|
| GET | `/api/items/{itemId}/variants` | List Variant milik satu Item |
| GET | `/api/variants/{id}` | Detail Variant |
| POST | `/api/variants` | Buat Variant (otomatis buat Stock awal) |
| PUT | `/api/variants/{id}` | Update Variant |
| DELETE | `/api/variants/{id}` | Soft delete Variant |

### Stock

| Method | Endpoint | Keterangan |
|---|---|---|
| GET | `/api/variants/{variantId}/stock` | Cek status stok |
| PATCH | `/api/variants/{variantId}/stock/adjust` | Restock / koreksi manual |
| POST | `/api/variants/{variantId}/stock/reserve` | Reservasi stok (checkout dimulai) |
| POST | `/api/variants/{variantId}/stock/release` | Lepas reservasi (order dibatalkan) |
| POST | `/api/variants/{variantId}/stock/commit` | Finalisasi (order sukses, stok fisik berkurang) |

### Purchase (Simulasi transaksi)

| Method | Endpoint | Keterangan |
|---|---|---|
| POST | `/api/purchases` | Beli banyak varian sekaligus, atomik (all-or-nothing) |

## Logic Anti-Oversell (Concurrency Handling)

Sistem mencegah penjualan barang yang stoknya habis lewat dua mekanisme yang saling melengkapi:

### 1. Pemisahan `quantityOnHand` vs `quantityReserved`

```
quantityAvailable = quantityOnHand - quantityReserved
```

- `quantityOnHand` → jumlah fisik barang di gudang.
- `quantityReserved` → jumlah yang sedang "dikunci" untuk transaksi yang belum final (mis. sedang di tahap checkout/pembayaran).
- `quantityAvailable` → yang benar-benar bisa dijual saat itu juga. Dihitung on-the-fly, tidak disimpan sebagai kolom terpisah supaya tidak ada risiko data tidak sinkron.

Alur order konvensional: **reserve** (saat checkout dimulai) → **commit** (saat pembayaran sukses, stok fisik dikurangi) atau **release** (saat dibatalkan, reservasi dilepas).

### 2. Optimistic Locking (`@Version`) di entity `Stock`

```java
@Version
private Long version;
```

Kalau dua request datang bersamaan mencoba mengubah stok varian yang sama, JPA/Hibernate otomatis mendeteksi konflik lewat kolom `version`. Request yang datang belakangan akan gagal commit dengan `ObjectOptimisticLockingFailureException`, ditangkap oleh `GlobalExceptionHandler` dan dikembalikan sebagai **HTTP 409 Conflict** — client bisa retry.

### 3. Pessimistic Locking untuk transaksi kritikal (`/api/purchases`)

Untuk endpoint `purchase` yang memproses banyak baris item sekaligus dalam satu transaksi, dipakai `SELECT ... FOR UPDATE` (`@Lock(LockModeType.PESSIMISTIC_WRITE)`) supaya baris stok benar-benar terkunci selama transaksi berjalan — mencegah request checkout lain menyerobot stok yang sama di detik yang sama.

### 4. Atomicity lintas item (`@Transactional`)

Kalau checkout berisi 3 item dan item ke-2 ternyata stoknya kurang, **seluruh transaksi di-rollback** — item ke-1 dan ke-3 yang sudah "sempat diproses" pun ikut dibatalkan. Ini mencegah kondisi stok berkurang sebagian padahal pembelian gagal.

## Contoh Skenario Testing

### Skenario sukses
```bash
curl -X POST "http://localhost:8080/api/purchases" \
  -H "Content-Type: application/json" \
  -d '{
    "customerName": "Budi Santoso",
    "lines": [ { "variantId": 1, "quantity": 2 } ]
  }'
```
Ekspektasi: `201 Created`, stok `quantityOnHand` berkurang 2.

### Skenario stok tidak cukup
```bash
curl -i -X POST "http://localhost:8080/api/variants/1/stock/reserve" \
  -H "Content-Type: application/json" \
  -d '{ "quantity": 999999 }'
```
Ekspektasi: `409 Conflict`, body berisi pesan `"Stok tidak mencukupi..."`.

### Skenario rollback multi-item
```bash
curl -i -X POST "http://localhost:8080/api/purchases" \
  -H "Content-Type: application/json" \
  -d '{
    "customerName": "Budi Santoso",
    "lines": [
      { "variantId": 1, "quantity": 1 },
      { "variantId": 2, "quantity": 999999 }
    ]
  }'
```
Ekspektasi: `409 Conflict`. Cek ulang `GET /api/variants/1/stock` — stok `variantId: 1` **tidak berubah** meskipun line pertama valid, karena line kedua gagal dan transaksi rollback total.

### Skenario race condition (opsional, manual test)
Jalankan dua `curl reserve` dengan `quantity` yang totalnya melebihi stok, dari dua terminal berbeda hampir bersamaan. Salah satu harus gagal dengan `409 Conflict` — membuktikan tidak ada dua transaksi yang lolos "menjual" unit stok yang sama.

## Error Handling

Semua error dikembalikan dalam format konsisten:
```json
{
  "status": 409,
  "message": "Stok tidak mencukupi. Tersedia: 3, diminta: 5",
  "timestamp": "2026-07-19T10:15:30"
}
```

| Exception | HTTP Status | Kapan terjadi |
|---|---|---|
| `ResourceNotFoundException` | 404 | Id yang direferensikan tidak ditemukan |
| `DuplicateResourceException` | 409 | Kode/SKU/nama sudah dipakai |
| `InsufficientStockException` | 409 | Stok tidak cukup untuk memenuhi permintaan |
| `ObjectOptimisticLockingFailureException` | 409 | Konflik konkurensi (dua request ubah data sama bersamaan) |
| `MethodArgumentNotValidException` | 400 | Body request tidak lolos validasi (`@NotBlank`, `@Min`, dll) |
