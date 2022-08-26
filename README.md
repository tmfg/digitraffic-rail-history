# Projekti
Sisältää lähdekoodin palvelulle [rata.digitraffic.fi/history/](https://rata.digitraffic.fi/history/)

# Kehitysympäristön asentaminen
1. Kloonaa repo `git clone https://github.com/tmfg/digitraffic-rail-history-private.git`
2. Valmistele mysql-tietokanta
   1. Luo tietokantaan schema `create schema trainhistoryjson;`
   2. Luo `train-history-updater/src/main/resources/application-localhost.properties`, jonne yhteysasetukset 
   ```
      spring.datasource.username=JOTAIN
      spring.datasource.password=JOTAIN
      spring.datasource.url=jdbc:mysql://localhost/trainhistoryjson?autoReconnect=true&rewriteBatchedStatements=true&cachePrepStmts=true&useTimezone=true&serverTimezone=UTC&useSSL=false
   ```
   3. Tee sama temppu `train-history-backend/src/main/resources/application-localhost.properties`:lle 
3. Käynnistä `TrainHistoryUpdaterApplication` `localhost`-profiililla ja tietokantaan pitäisi alkaa virrata dataa
4. Käynnistä `TrainHistoryBackendApplication` `localhost`-profiililla
5. Asenna `ng` tarvittaessa: `npm install -g @angular/cli`
6. Mene train-history-web hakemistoon ja aja `npm install`
7. Käynnistä web-käyttöliittymä `npm run start`

# Arkkitehtuuri

Historiapalvelu koostuu kolmesta komponentista:

* train-history-web
    * Web-sovellus
* train-history-backend
    * Web-sovelluksen taustajärjestelmä
* train-history-updater
    * Hakee ja tallentaa historiatiedot backend-sovelluksen luettavaksi
