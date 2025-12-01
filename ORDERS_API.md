# Orders API Documentation

## Order Endpoints

### 1. Get All Orders
**GET** `/api/orders`

Recupera tutti gli ordini (funzione admin).

**Headers:**
- `Authorization: Bearer <token>`

**Response:**
```json
{
  "success": true,
  "message": "Orders retrieved successfully",
  "data": [
    {
      "id": 1,
      "userId": 1,
      "orderDate": "2025-11-30T10:00:00",
      "totalPrice": 150.50,
      "status": "PENDING",
      "deliveryAddress": "Via Roma 123, Milano",
      "notes": "Consegna al mattino",
      "items": [
        {
          "id": 1,
          "itemType": "PRODUCT",
          "itemId": 5,
          "quantity": 2,
          "unitPrice": 25.00,
          "totalPrice": 50.00
        }
      ]
    }
  ]
}
```

### 2. Get Order by ID
**GET** `/api/orders/{id}`

Recupera un ordine specifico tramite ID.

**Headers:**
- `Authorization: Bearer <token>`

### 3. Get User Orders
**GET** `/api/orders/user/{userId}`

Recupera tutti gli ordini di un utente specifico.

**Headers:**
- `Authorization: Bearer <token>`

### 4. Get Current User Orders
**GET** `/api/orders/my`

Recupera tutti gli ordini dell'utente autenticato.

**Headers:**
- `Authorization: Bearer <token>`

### 5. Get Orders by Status
**GET** `/api/orders/status/{status}`

Recupera ordini per stato.

**Stati disponibili:**
- `PENDING` - In attesa
- `CONFIRMED` - Confermato
- `PROCESSING` - In elaborazione
- `SHIPPED` - Spedito
- `DELIVERED` - Consegnato
- `CANCELLED` - Annullato

**Headers:**
- `Authorization: Bearer <token>`

### 6. Get Current User Orders by Status
**GET** `/api/orders/my/status/{status}`

Recupera gli ordini dell'utente autenticato filtrati per stato.

**Headers:**
- `Authorization: Bearer <token>`

### 7. Create Order
**POST** `/api/orders`

Crea un nuovo ordine. Il sistema calcola automaticamente i prezzi e il totale.

**Headers:**
- `Authorization: Bearer <token>`

**Request Body:**
```json
{
  "items": [
    {
      "itemType": "PRODUCT",
      "itemId": 5,
      "quantity": 2
    },
    {
      "itemType": "PACKAGE",
      "itemId": 3,
      "quantity": 1
    }
  ],
  "deliveryAddress": "Via Roma 123, Milano",
  "notes": "Consegna al mattino"
}
```

**Validations:**
- `items`: Obbligatorio, almeno un item
- `itemType`: PRODUCT o PACKAGE
- `itemId`: ID del prodotto o pacchetto
- `quantity`: Minimo 1
- `deliveryAddress`: Obbligatorio

**Response:**
```json
{
  "success": true,
  "message": "Order created successfully",
  "data": {
    "id": 1,
    "userId": 1,
    "orderDate": "2025-11-30T10:00:00",
    "totalPrice": 150.50,
    "status": "PENDING",
    "deliveryAddress": "Via Roma 123, Milano",
    "notes": "Consegna al mattino",
    "items": [...]
  }
}
```

### 8. Update Order Status
**PUT** `/api/orders/{id}/status`

Aggiorna lo stato di un ordine.

**Headers:**
- `Authorization: Bearer <token>`

**Request Body:**
```json
{
  "status": "CONFIRMED"
}
```

### 9. Cancel Order
**PUT** `/api/orders/{id}/cancel`

Annulla un ordine. Solo ordini PENDING o CONFIRMED possono essere annullati.

**Headers:**
- `Authorization: Bearer <token>`

**Response:**
```json
{
  "success": true,
  "message": "Order cancelled successfully",
  "data": {...}
}
```

### 10. Delete Order
**DELETE** `/api/orders/{id}`

Elimina un ordine. Solo ordini PENDING o CANCELLED possono essere eliminati.

**Headers:**
- `Authorization: Bearer <token>`

### 11. Get User Statistics
**GET** `/api/orders/my/statistics`

Recupera statistiche sugli ordini dell'utente autenticato.

**Headers:**
- `Authorization: Bearer <token>`

**Response:**
```json
{
  "success": true,
  "message": "Statistics retrieved successfully",
  "data": {
    "totalOrders": 15,
    "totalSpent": 1250.75,
    "pendingOrders": 2,
    "completedOrders": 10
  }
}
```

---

## Order Status Flow

```
PENDING → CONFIRMED → PROCESSING → SHIPPED → DELIVERED
   ↓
CANCELLED (possibile solo da PENDING o CONFIRMED)
```

---

## Order Item Types

### PRODUCT
Ordine di un singolo prodotto.

### PACKAGE
Ordine di un pacchetto di prodotti.

---

## Business Rules

### Order Creation
✅ L'utente viene automaticamente recuperato dal token JWT  
✅ I prezzi vengono calcolati automaticamente dal sistema  
✅ Lo stato iniziale è sempre PENDING  
✅ La data ordine è impostata automaticamente  

### Order Cancellation
✅ Solo il proprietario può annullare il proprio ordine  
✅ Solo ordini PENDING o CONFIRMED possono essere annullati  
✅ Lo stato viene cambiato a CANCELLED  

### Order Deletion
✅ Solo il proprietario può eliminare il proprio ordine  
✅ Solo ordini PENDING o CANCELLED possono essere eliminati  
✅ L'eliminazione è permanente  

---

## Error Responses

### Product/Package Not Found
```json
{
  "success": false,
  "message": "Product not found with id: 5",
  "status": 400
}
```

### Cannot Cancel Order
```json
{
  "success": false,
  "message": "Order cannot be cancelled in current status: SHIPPED",
  "status": 400
}
```

### Unauthorized
```json
{
  "success": false,
  "message": "You can only cancel your own orders",
  "status": 400
}
```

---

## Features

✅ **CRUD completo** - Create, Read, Update, Delete  
✅ **Gestione stati** - Workflow completo degli stati ordine  
✅ **Calcolo automatico prezzi** - Il sistema calcola prezzi e totali  
✅ **Supporto prodotti e pacchetti** - Ordina sia prodotti singoli che pacchetti  
✅ **Validazione business rules** - Controlli su cancellazione ed eliminazione  
✅ **Statistiche utente** - Dashboard con statistiche ordini  
✅ **Autorizzazione** - Solo il proprietario può modificare i propri ordini  
✅ **Transazioni** - Operazioni atomiche per integrità dati  
✅ **Relazioni bidirezionali** - Order ↔ OrderItems  
