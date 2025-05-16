# Projekti

Sisältää lähdekoodin palvelulle
[rata.digitraffic.fi/history/](https://rata.digitraffic.fi/history/)

# Kehitysympäristön asentaminen

1. Kloonaa repo
   `git clone https://github.com/tmfg/digitraffic-rail-history-private.git`
2. Luo tietokanta ja aja migrat
   ```bash
   cd db
   docker compose up -d
   # tai 
   ./db-rm-build-up.sh
   ```
3. Luo
   `train-history-updater/src/main/resources/application-localhost.properties`,
   jonne yhteysasetukset. Kannan username ja password löytyy
   `db/docker-compose.yml`-tiedostosta (MYSQL_USER ja MYSQL_PASSWORD)
   ```properties
   spring.datasource.username=JOTAIN
   spring.datasource.password=JOTAIN
   spring.datasource.url=jdbc:mysql://localhost/trainhistoryjson?autoReconnect=true&rewriteBatchedStatements=true&cachePrepStmts=true&useTimezone=true&serverTimezone=UTC&useSSL=false
   ```
4. Tee sama temppu
   `train-history-backend/src/main/resources/application-localhost.properties`:lle
5. Käynnistä `TrainHistoryUpdaterApplication` `localhost`-profiililla
   `mvn spring-boot:run -Dspring-boot.run.profiles=localhost` ja tietokantaan
   pitäisi alkaa virrata dataa
6. Käynnistä `TrainHistoryBackendApplication` `localhost`-profiililla
   `mvn spring-boot:run -Dspring-boot.run.profiles=localhost`
7. Asenna `ng` tarvittaessa: `npm install -g @angular/cli`
8. Mene train-history-web hakemistoon ja aja `npm install`
9. Käynnistä web-käyttöliittymä `npm run dev` -> otaa yhteyden rata.digitraffic.fi tai `npm run dev:local`. Erona on,
   että mitä proxy filua käytetään. `dev` käytää rata.digitraffic.fi rajapintoja ja local lokaalisti pyörivää sovellusta.
10. Avaa selaimella  http://localhost:4200/

# Testit

### train-history-backend

Oletuksena että kehitysympäristö on pystytetty ml. tietokanta ja sovellusprofiili.

```
cd train-history-backend
mvn test -Dspring.profiles.active=localhost
```

# Arkkitehtuuri

Historiapalvelu koostuu kolmesta komponentista:

- train-history-web
  - Web-sovellus
- train-history-backend
  - Web-sovelluksen taustajärjestelmä
- train-history-updater
  - Hakee ja tallentaa historiatiedot backend-sovelluksen luettavaksi
