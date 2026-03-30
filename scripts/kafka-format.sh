#!/bin/bash
cd ~/kafka_2.13-4.2.0
bin/kafka-storage.sh format --standalone -t $(bin/kafka-storage.sh random-uuid) -c config/server.properties