FROM flyway/flyway:11.8.0-alpine

ENV MYSQL_DIRVER_VERSION=8.3.0

RUN cd /flyway/drivers && wget "https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/${MYSQL_DIRVER_VERSION}/mysql-connector-j-${MYSQL_DIRVER_VERSION}.jar"