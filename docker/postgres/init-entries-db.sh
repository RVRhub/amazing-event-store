#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE USER roman;
    CREATE DATABASE es;
    GRANT ALL PRIVILEGES ON DATABASE es TO roman;
    CREATE TABLE entries (
        id serial NOT NULL,
        title TEXT,
        body TEXT,
        CONSTRAINT pk PRIMARY KEY (id)
     );
EOSQL