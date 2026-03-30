-- Run once to set up local development database
-- psql -U $(whoami) -f init-db.sql

CREATE DATABASE shopstream_orders;
CREATE USER shop WITH PASSWORD 'shop';
GRANT ALL PRIVILEGES ON DATABASE shopstream_orders TO shop;