package it.unicam.cs.ids.agriplatform.models;

public enum OrderStatus {
    PENDING, // In attesa di conferma
    CONFIRMED, // Confermato
    PROCESSING, // In elaborazione
    SHIPPED, // Spedito
    DELIVERED, // Consegnato
    CANCELLED // Annullato
}
