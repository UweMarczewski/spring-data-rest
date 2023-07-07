# Aufbau
Es gibt eine Company Entity die eine Liste von Employees enthält.\
Jeder Employee kann / ist einer Company zugeordnet.\
Die Datenbank wird über ein Flyway Script erstellt.

### Starte Docker Container mit PostgreSQL Datenbank
```bash
docker-compose up -d
```

### Nach Applikationsstart kann HAL-Explorer unter folgender URL aufgerufen werden
[http://localhost:8080](http://localhost:8080)
### Falls nötig kann auf die Datenbank mit folgendem Befehl zugegriffen werden
```bash
./connectToDb.sh
``` 