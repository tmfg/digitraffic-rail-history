FROM flyway/flyway:12.3.0-alpine

ENV MYSQL_DRIVER_VERSION=8.3.0

RUN cd /flyway/drivers && wget "https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/${MYSQL_DRIVER_VERSION}/mysql-connector-j-${MYSQL_DRIVER_VERSION}.jar"