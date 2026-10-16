# expense-tracker-api

# Link to roadmap.sh:
- https://roadmap.sh/projects/expense-tracker-api

## Starten

Voraussetzung: JDK 25 und Docker Desktop.

1. PostgreSQL starten:

   ```powershell
   docker compose up -d
   ```

2. Die Anwendung starten:

   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

Alternativ in IntelliJ die Run-Konfiguration `ExpenseTrackerApiApplication`
auswählen und starten. In IntelliJ muss das Projekt als Maven-Projekt geladen
sein; falls die Run-Konfiguration nicht erscheint, `pom.xml` als Maven-Projekt
hinzufügen bzw. die Maven-Projekte neu laden.