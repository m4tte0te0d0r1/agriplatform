# Events API Documentation

## Event Endpoints

### 1. Get All Events
**GET** `/api/events`

Recupera tutti gli eventi ordinati per data.

**Response:**
```json
{
  "success": true,
  "message": "Events retrieved successfully",
  "data": [
    {
      "id": 1,
      "name": "Fiera Agricola 2025",
      "description": "Grande evento agricolo",
      "eventDate": "2025-12-15T10:00:00",
      "location": "Milano"
    }
  ]
}
```

### 2. Get Upcoming Events
**GET** `/api/events/upcoming`

Recupera solo gli eventi futuri (dopo la data corrente).

### 3. Get Past Events
**GET** `/api/events/past`

Recupera gli eventi passati.

### 4. Get Event by ID
**GET** `/api/events/{id}`

Recupera un evento specifico tramite ID.

### 5. Search Events by Name
**GET** `/api/events/search/name?q={searchTerm}`

Cerca eventi per nome (case insensitive).

**Example:** `/api/events/search/name?q=fiera`

### 6. Search Events by Location
**GET** `/api/events/search/location?q={searchTerm}`

Cerca eventi per località (case insensitive).

**Example:** `/api/events/search/location?q=milano`

### 7. Create Event
**POST** `/api/events`

Crea un nuovo evento.

**Headers:**
- `Authorization: Bearer <token>`

**Request Body:**
```json
{
  "name": "Fiera Agricola 2025",
  "description": "Grande evento agricolo",
  "eventDate": "2025-12-15T10:00:00",
  "location": "Milano"
}
```

**Validations:**
- `name`: Obbligatorio
- `description`: Obbligatorio
- `eventDate`: Obbligatorio, deve essere nel futuro
- `location`: Obbligatorio

### 8. Update Event
**PUT** `/api/events/{id}`

Aggiorna un evento esistente.

**Headers:**
- `Authorization: Bearer <token>`

**Request Body:**
```json
{
  "name": "Fiera Agricola 2025 - Aggiornato",
  "description": "Descrizione aggiornata",
  "eventDate": "2025-12-16T10:00:00",
  "location": "Milano Fiera"
}
```

### 9. Delete Event
**DELETE** `/api/events/{id}`

Elimina un evento e tutti i suoi inviti.

**Headers:**
- `Authorization: Bearer <token>`

---

## Event Invites Endpoints

### 10. Invite User to Event
**POST** `/api/events/{eventId}/invite/{userId}`

Invita un utente a un evento.

**Headers:**
- `Authorization: Bearer <token>`

**Response:**
```json
{
  "success": true,
  "message": "User invited successfully",
  "data": {
    "id": 1,
    "eventId": 1,
    "userId": 2
  }
}
```

### 11. Get Event Invites
**GET** `/api/events/{eventId}/invites`

Recupera tutti gli inviti per un evento specifico.

**Headers:**
- `Authorization: Bearer <token>`

### 12. Get User Invited Events
**GET** `/api/events/invites/user/{userId}`

Recupera tutti gli eventi a cui un utente è invitato.

**Headers:**
- `Authorization: Bearer <token>`

### 13. Get Current User Invited Events
**GET** `/api/events/invites/my`

Recupera gli eventi a cui l'utente autenticato è invitato.

**Headers:**
- `Authorization: Bearer <token>`

### 14. Remove Invite
**DELETE** `/api/events/{eventId}/invite/{userId}`

Rimuove un invito.

**Headers:**
- `Authorization: Bearer <token>`

### 15. Check Invite Status
**GET** `/api/events/{eventId}/invited/{userId}`

Verifica se un utente è invitato a un evento.

**Headers:**
- `Authorization: Bearer <token>`

**Response:**
```json
{
  "success": true,
  "message": "Invite status checked",
  "data": true
}
```

---

## Error Responses

### Event Not Found
```json
{
  "success": false,
  "message": "Event not found with id: 1",
  "status": 404
}
```

### User Already Invited
```json
{
  "success": false,
  "message": "User is already invited to this event",
  "status": 400
}
```

### Validation Error
```json
{
  "success": false,
  "message": "Validation failed",
  "errors": {
    "eventDate": "Event date must be in the future"
  },
  "status": 400
}
```

---

## Features

✅ **CRUD completo** per eventi  
✅ **Gestione inviti** - invita utenti agli eventi  
✅ **Ricerca** - per nome e località  
✅ **Filtri temporali** - eventi futuri/passati  
✅ **Validazione** - data evento deve essere futura  
✅ **Autorizzazione** - richiede autenticazione per operazioni sensibili  
✅ **Cascade delete** - eliminando un evento si eliminano anche gli inviti  
