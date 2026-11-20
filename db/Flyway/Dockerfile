FROM flyway/flyway:13.12.2-alpine@sha256:f5b9740a05ef38fc31356dcf2e7796dafc693ea289546e82c7699d37f83349af

ENV MYSQL_DRIVER_VERSION=8.3.0

RUN cd /flyway/drivers && wget "https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/${MYSQL_DRIVER_VERSION}/mysql-connector-j-${MYSQL_DRIVER_VERSION}.jar"