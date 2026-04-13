# AutoTrack360 – API Documentation

**Base URL:** `http://localhost:8080`  
**Auth:** All endpoints (except `/api/auth/**`) require a JWT token in the header:
```
Authorization: Bearer <token>
```

---

## Table of Contents

1. [Authentication](#1-authentication)
2. [Users](#2-users)
3. [Vehicles](#3-vehicles)
4. [Shipments](#4-shipments)
5. [Inventory](#5-inventory)
6. [Sales & Customers](#6-sales--customers)
7. [Payments](#7-payments)
8. [Documents](#8-documents)
9. [Dashboard](#9-dashboard)
10. [Enums Reference](#10-enums-reference)
11. [Error Responses](#11-error-responses)

---

## 1. Authentication

### POST `/api/auth/register`
Register a new user.

**Request Body:**
```json
{
  "name": "John Doe",
  "email": "john@autotrack360.com",
  "password": "secret123",
  "role": "ADMIN"
}
```

**Response `200 OK`:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "email": "john@autotrack360.com",
  "role": "ADMIN"
}
```

---

### POST `/api/auth/login`
Login and receive a JWT token.

**Request Body:**
```json
{
  "email": "admin@autotrack360.com",
  "password": "admin123"
}
```

**Response `200 OK`:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "email": "admin@autotrack360.com",
  "role": "ADMIN"
}
```

> **Seeded accounts (demo):**
> | Email | Password | Role |
> |---|---|---|
> | admin@autotrack360.com | admin123 | ADMIN |
> | sales@autotrack360.com | sales123 | SALES |
> | logistics@autotrack360.com | logistics123 | LOGISTICS |

---

## 2. Users

### GET `/api/users`
Get all registered users.

**Response `200 OK`:**
```json
[
  {
    "id": 1,
    "name": "Admin User",
    "email": "admin@autotrack360.com",
    "role": "ADMIN"
  },
  {
    "id": 2,
    "name": "Sales Agent",
    "email": "sales@autotrack360.com",
    "role": "SALES"
  }
]
```

---

### GET `/api/users/{id}`
Get a single user by ID.

**Path Params:** `id` — user ID

**Response `200 OK`:**
```json
{
  "id": 1,
  "name": "Admin User",
  "email": "admin@autotrack360.com",
  "role": "ADMIN"
}
```

---

## 3. Vehicles

### POST `/api/vehicles`
Create a new vehicle.

**Request Body:**
```json
{
  "vin": "1HGCM82633A004352",
  "make": "Toyota",
  "model": "Land Cruiser",
  "year": 2023,
  "color": "White",
  "status": "IMPORTED",
  "price": 75000.00
}
```

> `status` is optional — defaults to `IMPORTED` if not provided.

**Response `200 OK`:**
```json
{
  "id": 4,
  "vin": "1HGCM82633A004352",
  "make": "Toyota",
  "model": "Land Cruiser",
  "year": 2023,
  "color": "White",
  "status": "IMPORTED",
  "price": 75000.00
}
```

---

### GET `/api/vehicles`
Get all vehicles.

**Response `200 OK`:**
```json
[
  {
    "id": 1,
    "vin": "1HGCM82633A123456",
    "make": "Toyota",
    "model": "Land Cruiser",
    "year": 2023,
    "color": "White",
    "status": "AVAILABLE",
    "price": 75000.00
  },
  {
    "id": 2,
    "vin": "2T1BURHE0JC123457",
    "make": "Honda",
    "model": "CR-V",
    "year": 2022,
    "color": "Black",
    "status": "IMPORTED",
    "price": 35000.00
  }
]
```

---

### GET `/api/vehicles/{id}`
Get a single vehicle by ID.

**Path Params:** `id` — vehicle ID

**Response `200 OK`:**
```json
{
  "id": 1,
  "vin": "1HGCM82633A123456",
  "make": "Toyota",
  "model": "Land Cruiser",
  "year": 2023,
  "color": "White",
  "status": "AVAILABLE",
  "price": 75000.00
}
```

---

### PUT `/api/vehicles/{id}`
Update vehicle details (does not change VIN or status).

**Path Params:** `id` — vehicle ID

**Request Body:**
```json
{
  "make": "Toyota",
  "model": "Land Cruiser 200",
  "year": 2023,
  "color": "Pearl White",
  "price": 78000.00
}
```

**Response `200 OK`:**
```json
{
  "id": 1,
  "vin": "1HGCM82633A123456",
  "make": "Toyota",
  "model": "Land Cruiser 200",
  "year": 2023,
  "color": "Pearl White",
  "status": "AVAILABLE",
  "price": 78000.00
}
```

---

### PATCH `/api/vehicles/{id}/status?status={status}`
Change the status of a vehicle.

**Path Params:** `id` — vehicle ID  
**Query Params:** `status` — one of `IMPORTED | IN_TRANSIT | ARRIVED | AVAILABLE | SOLD`

**Example:**
```
PATCH /api/vehicles/1/status?status=AVAILABLE
```

**Response `200 OK`:**
```json
{
  "id": 1,
  "vin": "1HGCM82633A123456",
  "make": "Toyota",
  "model": "Land Cruiser",
  "year": 2023,
  "color": "White",
  "status": "AVAILABLE",
  "price": 75000.00
}
```

---

## 4. Shipments

### POST `/api/shipments`
Create a new shipment. Status defaults to `CREATED`.

**Request Body:**
```json
{
  "name": "Shipment #001",
  "origin": "Japan",
  "destination": "Kigali, Rwanda"
}
```

**Response `200 OK`:**
```json
{
  "id": 1,
  "name": "Shipment #001",
  "origin": "Japan",
  "destination": "Kigali, Rwanda",
  "status": "CREATED",
  "vehicles": []
}
```

---

### GET `/api/shipments`
Get all shipments.

**Response `200 OK`:**
```json
[
  {
    "id": 1,
    "name": "Shipment #001",
    "origin": "Japan",
    "destination": "Kigali, Rwanda",
    "status": "SHIPPED",
    "vehicles": [
      {
        "id": 2,
        "vin": "2T1BURHE0JC123457",
        "make": "Honda",
        "model": "CR-V",
        "year": 2022,
        "color": "Black",
        "status": "IN_TRANSIT",
        "price": 35000.00
      }
    ]
  }
]
```

---

### GET `/api/shipments/{id}`
Get a single shipment by ID.

**Path Params:** `id` — shipment ID

**Response `200 OK`:** *(same structure as above, single object)*

---

### POST `/api/shipments/{id}/vehicles/{vehicleId}`
Add a vehicle to a shipment. Vehicle status is automatically set to `IN_TRANSIT`.

**Path Params:**
- `id` — shipment ID
- `vehicleId` — vehicle ID

**Response `200 OK`:**
```json
{
  "id": 1,
  "name": "Shipment #001",
  "origin": "Japan",
  "destination": "Kigali, Rwanda",
  "status": "CREATED",
  "vehicles": [
    {
      "id": 2,
      "vin": "2T1BURHE0JC123457",
      "make": "Honda",
      "model": "CR-V",
      "year": 2022,
      "color": "Black",
      "status": "IN_TRANSIT",
      "price": 35000.00
    }
  ]
}
```

---

### PATCH `/api/shipments/{id}/status?status={status}`
Update shipment status. When set to `ARRIVED`, all vehicles in the shipment are automatically set to `ARRIVED`.

**Path Params:** `id` — shipment ID  
**Query Params:** `status` — one of `CREATED | SHIPPED | ARRIVED`

**Example:**
```
PATCH /api/shipments/1/status?status=ARRIVED
```

**Response `200 OK`:**
```json
{
  "id": 1,
  "name": "Shipment #001",
  "origin": "Japan",
  "destination": "Kigali, Rwanda",
  "status": "ARRIVED",
  "vehicles": [
    {
      "id": 2,
      "vin": "2T1BURHE0JC123457",
      "make": "Honda",
      "model": "CR-V",
      "year": 2022,
      "color": "Black",
      "status": "ARRIVED",
      "price": 35000.00
    }
  ]
}
```

---

## 5. Inventory

### POST `/api/inventory/vehicle/{vehicleId}?location={location}`
Add a vehicle to inventory. Vehicle status is automatically set to `AVAILABLE`.

**Path Params:** `vehicleId` — vehicle ID  
**Query Params:** `location` — storage location (e.g. `"Lot-A"`)

**Example:**
```
POST /api/inventory/vehicle/2?location=Lot-A
```

**Response `200 OK`:**
```json
{
  "id": 1,
  "vehicle": {
    "id": 2,
    "vin": "2T1BURHE0JC123457",
    "make": "Honda",
    "model": "CR-V",
    "year": 2022,
    "color": "Black",
    "status": "AVAILABLE",
    "price": 35000.00
  },
  "location": "Lot-A",
  "status": "AVAILABLE"
}
```

---

### GET `/api/inventory`
Get all inventory items.

**Response `200 OK`:**
```json
[
  {
    "id": 1,
    "vehicle": {
      "id": 2,
      "vin": "2T1BURHE0JC123457",
      "make": "Honda",
      "model": "CR-V",
      "year": 2022,
      "color": "Black",
      "status": "AVAILABLE",
      "price": 35000.00
    },
    "location": "Lot-A",
    "status": "AVAILABLE"
  }
]
```

---

## 6. Sales & Customers

### POST `/api/sales/customers`
Create a new customer.

**Request Body:**
```json
{
  "name": "Alice Mutoni",
  "phone": "+250788123456",
  "email": "alice@example.com"
}
```

**Response `200 OK`:**
```json
{
  "id": 1,
  "name": "Alice Mutoni",
  "phone": "+250788123456",
  "email": "alice@example.com"
}
```

---

### GET `/api/sales/customers`
Get all customers.

**Response `200 OK`:**
```json
[
  {
    "id": 1,
    "name": "Alice Mutoni",
    "phone": "+250788123456",
    "email": "alice@example.com"
  }
]
```

---

### POST `/api/sales`
Create a new sale. Status defaults to `PENDING`. If the vehicle is in inventory, its inventory status is set to `RESERVED`.

**Request Body:**
```json
{
  "customerId": 1,
  "vehicleId": 1,
  "totalAmount": 75000.00
}
```

**Response `200 OK`:**
```json
{
  "id": 1,
  "customer": {
    "id": 1,
    "name": "Alice Mutoni",
    "phone": "+250788123456",
    "email": "alice@example.com"
  },
  "vehicle": {
    "id": 1,
    "vin": "1HGCM82633A123456",
    "make": "Toyota",
    "model": "Land Cruiser",
    "year": 2023,
    "color": "White",
    "status": "AVAILABLE",
    "price": 75000.00
  },
  "totalAmount": 75000.00,
  "status": "PENDING"
}
```

---

### GET `/api/sales`
Get all sales.

**Response `200 OK`:**
```json
[
  {
    "id": 1,
    "customer": { ... },
    "vehicle": { ... },
    "totalAmount": 75000.00,
    "status": "PENDING"
  }
]
```

---

### GET `/api/sales/{id}`
Get a single sale by ID.

**Path Params:** `id` — sale ID

**Response `200 OK`:** *(same structure as above, single object)*

---

### PATCH `/api/sales/{id}/complete`
Mark a sale as completed. Vehicle status is set to `SOLD` and inventory status is set to `SOLD`.

**Path Params:** `id` — sale ID

**Response `200 OK`:**
```json
{
  "id": 1,
  "customer": {
    "id": 1,
    "name": "Alice Mutoni",
    "phone": "+250788123456",
    "email": "alice@example.com"
  },
  "vehicle": {
    "id": 1,
    "vin": "1HGCM82633A123456",
    "make": "Toyota",
    "model": "Land Cruiser",
    "year": 2023,
    "color": "White",
    "status": "SOLD",
    "price": 75000.00
  },
  "totalAmount": 75000.00,
  "status": "COMPLETED"
}
```

---

## 7. Payments

### POST `/api/payments`
Record a payment for a sale.

**Request Body:**
```json
{
  "saleId": 1,
  "amount": 75000.00
}
```

**Response `200 OK`:**
```json
{
  "id": 1,
  "saleId": 1,
  "amount": 75000.00,
  "date": "2024-06-15"
}
```

---

### GET `/api/payments`
Get all payments.

**Response `200 OK`:**
```json
[
  {
    "id": 1,
    "saleId": 1,
    "amount": 75000.00,
    "date": "2024-06-15"
  }
]
```

---

### GET `/api/payments/sale/{saleId}`
Get all payments for a specific sale.

**Path Params:** `saleId` — sale ID

**Response `200 OK`:**
```json
[
  {
    "id": 1,
    "saleId": 1,
    "amount": 40000.00,
    "date": "2024-06-10"
  },
  {
    "id": 2,
    "saleId": 1,
    "amount": 35000.00,
    "date": "2024-06-15"
  }
]
```

---

## 8. Documents

### POST `/api/documents/upload`
Upload a file and link it to a vehicle or sale.

**Content-Type:** `multipart/form-data`

**Form Fields:**
| Field | Type | Description |
|---|---|---|
| `file` | File | The file to upload |
| `relatedType` | String | `VEHICLE` or `SALE` |
| `relatedId` | Long | ID of the related vehicle or sale |

**Example (Postman):**
```
POST /api/documents/upload
Form-data:
  file       → [select file]
  relatedType → VEHICLE
  relatedId   → 1
```

**Response `200 OK`:**
```json
{
  "id": 1,
  "name": "invoice.pdf",
  "filePath": "uploads/a3f2c1d0-uuid_invoice.pdf",
  "relatedType": "VEHICLE",
  "relatedId": 1
}
```

---

### GET `/api/documents/{id}/download`
Download a file by document ID.

**Path Params:** `id` — document ID

**Response `200 OK`:**  
Returns the file as a binary stream with header:
```
Content-Disposition: attachment; filename="invoice.pdf"
Content-Type: application/octet-stream
```

---

### GET `/api/documents?relatedType={type}&relatedId={id}`
List all documents linked to a vehicle or sale.

**Query Params:**
- `relatedType` — `VEHICLE` or `SALE`
- `relatedId` — ID of the related entity

**Example:**
```
GET /api/documents?relatedType=VEHICLE&relatedId=1
```

**Response `200 OK`:**
```json
[
  {
    "id": 1,
    "name": "invoice.pdf",
    "filePath": "uploads/a3f2c1d0-uuid_invoice.pdf",
    "relatedType": "VEHICLE",
    "relatedId": 1
  },
  {
    "id": 2,
    "name": "customs_clearance.pdf",
    "filePath": "uploads/b9e1a2f3-uuid_customs_clearance.pdf",
    "relatedType": "VEHICLE",
    "relatedId": 1
  }
]
```

---

## 9. Dashboard

### GET `/api/dashboard`
Get a summary of key system metrics.

**Response `200 OK`:**
```json
{
  "totalVehicles": 10,
  "totalSales": 4,
  "availableInventory": 5,
  "completedSales": 3,
  "pendingSales": 1
}
```

---

## 10. Enums Reference

### Role
| Value | Description |
|---|---|
| `ADMIN` | Full system access |
| `SALES` | Manage sales and customers |
| `LOGISTICS` | Manage shipments and inventory |

### VehicleStatus
| Value | Description |
|---|---|
| `IMPORTED` | Vehicle record created, not yet shipped |
| `IN_TRANSIT` | Vehicle added to an active shipment |
| `ARRIVED` | Shipment marked as arrived |
| `AVAILABLE` | Vehicle added to inventory, ready for sale |
| `SOLD` | Sale completed |

### ShipmentStatus
| Value | Description |
|---|---|
| `CREATED` | Shipment created, not yet dispatched |
| `SHIPPED` | Shipment is on the way |
| `ARRIVED` | Shipment has arrived at destination |

### InventoryStatus
| Value | Description |
|---|---|
| `AVAILABLE` | Vehicle is available for sale |
| `RESERVED` | Vehicle is linked to a pending sale |
| `SOLD` | Vehicle has been sold |

### SaleStatus
| Value | Description |
|---|---|
| `PENDING` | Sale created, awaiting completion |
| `COMPLETED` | Sale finalized, vehicle marked as sold |

### RelatedType (Documents)
| Value | Description |
|---|---|
| `VEHICLE` | Document linked to a vehicle |
| `SALE` | Document linked to a sale |

---

## 11. Error Responses

All errors return a JSON object with an `error` field.

**`400 Bad Request`** — validation or business logic error:
```json
{
  "error": "Email already in use"
}
```

**`401 Unauthorized`** — missing or invalid JWT token:
```json
{
  "error": "Full authentication is required to access this resource"
}
```

**`500 Internal Server Error`** — unexpected server error:
```json
{
  "error": "could not execute statement"
}
```

---

## Typical Frontend Flow

```
1. POST /api/auth/login              → get JWT token
2. POST /api/vehicles                → register imported vehicle
3. POST /api/shipments               → create shipment
4. POST /api/shipments/1/vehicles/1  → add vehicle to shipment
5. PATCH /api/shipments/1/status?status=SHIPPED
6. PATCH /api/shipments/1/status?status=ARRIVED
7. POST /api/inventory/vehicle/1?location=Lot-A  → add to inventory
8. POST /api/sales/customers         → register customer
9. POST /api/sales                   → create sale
10. POST /api/payments               → record payment
11. PATCH /api/sales/1/complete      → finalize sale
12. POST /api/documents/upload       → attach documents
13. GET  /api/dashboard              → view summary stats
```

---

*AutoTrack360 — Vehicle Import and Sales Management System*  
*Base URL: `http://localhost:8080` | Swagger UI: `http://localhost:8080/swagger-ui/index.html`*
