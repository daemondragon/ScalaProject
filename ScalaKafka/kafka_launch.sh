#!/usr/bin/env bash

# TODO: adapt for need
KAFKA_DIRECTORY="../kafka_2.12-2.2.0"
KAFKA_PORT=2181
KAFKA_TOPIC_NAME="drone"

#Starting zookeeper
"$KAFKA_DIRECTORY/bin/zookeeper-server-start.sh" "$KAFKA_DIRECTORY/config/zookeeper.properties" &

#Waiting for zookeeper to be launched
sleep 2

#Starting kafka
"$KAFKA_DIRECTORY/bin/kafka-server-start.sh" "$KAFKA_DIRECTORY/config/server.properties" &

#Waiting for kafka to be launched
sleep 5

#Creating a topic
#"$KAFKA_DIRECTORY/bin/kafka-topics.sh" --create --zookeeper "localhost:$KAFKA_PORT" --replication-factor 1 --partitions 10 --topic "$KAFKA_TOPIC_NAME" &
