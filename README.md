# ShopStream — local dev setup

## Prerequisites
- Postgres.app running on port 5432
- Kafka 2.13-4.2.0 extracted to home folder

## Database setup (run once)
```bash
psql -U $(whoami) -f init-db.sql
```

## Kafka setup (run once)
```bash
./scripts/kafka-format.sh
```

## Start Kafka
```bash
./scripts/kafka-start.sh
```

## Start the app
```bash
mvn spring-boot:run
```

## Watch Kafka topic
```bash
./scripts/kafka-consumer.sh
```