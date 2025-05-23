# Project

Contains the source code for the service
[rata.digitraffic.fi/history/](https://rata.digitraffic.fi/history/)

# Setting up the development environment

1. Clone the repository:
   `git clone https://github.com/tmfg/digitraffic-rail-history-private.git`
2. Create the database and run migrations:
   ```bash
   cd db
   docker compose up -d
   # or
   ./db-rm-build-up.sh
   ```
3. Create the file
   `train-history-updater/src/main/resources/application-localhost.properties` and include the db connection properties. The database username and password can be found in `db/docker-compose.yml` as `MYSQL_USER` and `MYSQL_PASSWORD`.
   ```properties
   spring.datasource.username=JOTAIN
   spring.datasource.password=JOTAIN
   spring.datasource.url=jdbc:mysql://localhost/trainhistoryjson?autoReconnect=true&rewriteBatchedStatements=true&cachePrepStmts=true&useTimezone=true&serverTimezone=UTC&useSSL=false
   ```
4. Do the same for
   `train-history-backend/src/main/resources/application-localhost.properties`
5. Start `TrainHistoryUpdaterApplication` using the profile `localhost`
   `mvn spring-boot:run -Dspring-boot.run.profiles=localhost` and you should start getting data into the database.
6. Start `TrainHistoryBackendApplication` using the profile `localhost`
   `mvn spring-boot:run -Dspring-boot.run.profiles=localhost`
7. Install `ng` if necessary: `npm install -g @angular/cli`
8. Go the the directory `train-history-web` and run `npm install`
9. Start the web UI with `npm run dev` -> connects to rata.digitraffic.fi, or use `npm run dev:local`. The difference is which proxy file is used. `dev` uses rata.digitraffic.fi APIs while local connects to the locally running application.
10. Open a browser at  http://localhost:4200/

# Tests

### train-history-backend

When the development environment is set up, including the database and application profile, run:

```
cd train-history-backend
mvn test -Dspring.profiles.active=localhost
```

# Architecture

The history service consists of three components:

- train-history-web
  - Web application
- train-history-backend
  - Backend system for the web application
- train-history-updater
  - Fetches and stores historical data for the backend application to read
