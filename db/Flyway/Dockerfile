FROM flyway/flyway:9.21.1-alpine

ENV MYSQL_DIRVER_VERSION=8.1.0

RUN cd /flyway/drivers && wget "https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/${MYSQL_DIRVER_VERSION}/mysql-connector-j-${MYSQL_DIRVER_VERSION}.jar"