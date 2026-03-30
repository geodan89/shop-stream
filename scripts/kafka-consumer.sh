#!/bin/bash
cd ~/kafka_2.13-4.2.0
bin/kafka-console-consumer.sh \
  --bootstrap-server localhost:9092 \
  --topic orders.placed \
  --from-beginning