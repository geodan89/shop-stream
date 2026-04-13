#!/bin/bash
cd ~/kafka_2.13-4.2.0
bin/kafka-topics.sh --create \
  --topic orders.placed \
  --partitions 3 \
  --replication-factor 1 \
  --bootstrap-server localhost:9092