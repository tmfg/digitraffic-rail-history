FROM flyway/flyway:13.3.0-alpine@sha256:fb13953f16913efd313f202ec4b4ed34fd2ea14cc51401eb36ea9818c0df2918

ENV MYSQL_DRIVER_VERSION=8.3.0

RUN cd /flyway/drivers && wget "https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/${MYSQL_DRIVER_VERSION}/mysql-connector-j-${MYSQL_DRIVER_VERSION}.jar"