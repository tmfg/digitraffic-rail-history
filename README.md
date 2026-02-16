# Project

Contains the source code for the service
[rata.digitraffic.fi/history/](https://rata.digitraffic.fi/history/)

# Setting up the development environment

1. Clone the repository:
   `git clone https://github.com/tmfg/digitraffic-rail-history-private.git`
2. Initilize git submodule:
   `mvn initialize`
3. Create the database and run migrations:
   ```bash
   cd db
   ./db-rm-build-up.sh 
   ```
4. Start `TrainHistoryUpdaterApplication` using the profile `localhost`
   `mvn spring-boot:run` and you should start getting data into the database.
5. Start `TrainHistoryBackendApplication` using the profile `localhost`
   `mvn spring-boot:run`
6. Open a browser at  http://localhost:26087/history (port is the `server.port`)

# Run the applications with a different profiles

Create profile files
* `train-history-updater/src/main/resources/application-localhost.properties`
* `train-history-backend/src/main/resources/application-localhost.properties`

Use `application.properties` files as a base. 

Then run:

    mvn spring-boot:run -Dspring-boot.run.profiles=localhost

# Tests

## Train history backend

When the development environment is set up, including the database and application profile, run:

```
cd train-history-backend
mvn test
```

## E2E tests

You need to have both `train-history-updater` and `train-history-backend` running with the `default` profile and
the database up and running.

Before running the tests, you probably need to make slight modifications to the test files themselves.
This is because they are using the current date in searches for train data and most likely
your local database does not have that data. At the time of writing, this means that you need to change
the arguments given to `submitTrainInfoForm` function with something that exists in your local database.

Test uses `.env.playwright` properties.

To run the E2E tests:
```
cd train-history-backend
pnpm install
pnpm run install-playwright
pnpm run e2e-ui
```

# Architecture

The history service consists of two components:

- train-history-backend
  - Backend system
- train-history-updater
  - Fetches and stores historical data for the backend application to read
