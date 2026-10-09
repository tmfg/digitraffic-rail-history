FROM flyway/flyway:13.9.0-alpine@sha256:ae8e985a8caf73181a39aad823690f27d55c850d4c5cea92a94c0ce4b3943156

ENV MYSQL_DRIVER_VERSION=8.3.0

RUN cd /flyway/drivers && wget "https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/${MYSQL_DRIVER_VERSION}/mysql-connector-j-${MYSQL_DRIVER_VERSION}.jar"