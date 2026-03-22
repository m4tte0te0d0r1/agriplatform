# README

## Piattaforma di Digitalizzazione e Valorizzazione della Filiera Agricola Locale

### Descrizione del Progetto

Il progetto **AgriPlatform** mira a creare una piattaforma per la gestione, valorizzazione e tracciabilità dei prodotti agricoli di un territorio comunale. L'obiettivo principale è promuovere il territorio consentendo a tutti gli utenti di comprendere la provenienza e la qualità dei prodotti tipici locali. La piattaforma offre funzionalità per:
- Caricare e condividere informazioni sulla filiera agricola (produzione, trasformazione, vendita).
- Facilitare la tracciabilità di ogni prodotto e dell'intero ciclo produttivo.
- Promuovere e gestire eventi locali, fiere e visite guidate alle aziende.
- Organizzare i punti della filiera in un marketplace dove è possibile acquistare sia prodotti singoli che pacchetti e prenotare esperienze sul territorio.

### Attori Coinvolti
La piattaforma supporta diversi ruoli, tra cui:
1. **Produttore**: Carica informazioni sui prodotti, certificazioni e vende nel marketplace.
2. **Trasformatore**: Gestisce i processi di trasformazione, collega le fasi produttive e vende.
3. **Distributore di Tipicità**: Vende prodotti e crea "pacchetti gastronomici" combinati.
4. **Curatore**: Verifica e approva contenuti, garantendo l'affidabilità delle informazioni.
5. **Animatore della Filiera**: Organizza eventi, fiere, e visite alle aziende.
6. **Acquirente**: Acquista prodotti e si prenota per eventi.
7. **Utente Generico**: Accede per informarsi.
8. **Gestore della Piattaforma (Admin)**: Gestisce gli accreditamenti e l'amministrazione generale.

### Struttura Tecnica
Il progetto (lato backend) è stato sviluppato in **Java 17+** basandosi sul framework **Spring Boot (v3.1.4)**. Fornisce un'API REST fully-featured ed è collegato a un database **SQLite** per la semplicità di deployment, integrato con **Hibernate (JPA)** per la persistenza e **Spring Security + JWT** per l'autenticazione/autorizzazione degli utenti.

### Funzionalità Implementate
- **Autenticazione e Autorizzazione**: Registrazione, login e gestione ruoli tramite JWT.
- **Gestione Prodotti e Marketplace**: CRUD completo dei prodotti tramite validazione a stati (approvazione del curatore).
- **Gestione Ordini**: Workflow degli ordini per gli acquirenti dal checkout alla spedizione e resoconto statistiche.
- **Gestione Eventi**: Creazione ed inviti a fiere e tour enogastronomici gestiti dall'Animatore e fruibili dagli utenti.

### Come Avviare l'Applicazione

1. Assicurati di avere **Java 17 o superiore** installato.
2. Assicurati di avere **Maven** installato (o di usare un IDE che lo supporti nativamente).
3. Apri un terminale nella cartella `backend/agriplatform`.
4. Esegui il comando:
   ```bash
   mvn clean spring-boot:run
   ```
5. L'applicazione verrà avviata sulla porta `5001`. Il database SQLite (`agriplatform.db`) verrà automaticamente inizializzato nella directory locale di avvio.
