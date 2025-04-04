#!/bin/bash

# Коннектор для записи в файл
curl -X PUT http://localhost:8083/connectors/file-stream-sink/config \
  -H "Content-Type: application/json" \
  --data '{
    "name": "file-stream-sink",
    "connector.class": "org.apache.kafka.connect.file.FileStreamSinkConnector",
    "tasks.max": "1",
    "topics": "app-filtered-goods",
    "file": "/data/filtered-goods.txt",
    "value.converter": "org.apache.kafka.connect.storage.StringConverter"
    }' | jq

# Коннектор для записи в HDFS
curl -X PUT http://localhost:8083/connectors/hdfs/config \
  -H "Content-Type: application/json" \
  --data '{
    "connector.class": "io.confluent.connect.hdfs3.Hdfs3SinkConnector",
    "tasks.max": "1",
    "topics": "source.app-search-queries",
    "hdfs.url": "hdfs://hadoop-namenode:9000",
    "flush.size": "1",
    "key.converter": "org.apache.kafka.connect.storage.StringConverter",
    "value.converter": "org.apache.kafka.connect.storage.StringConverter",
    "format.class":"io.confluent.connect.hdfs3.string.StringFormat",
    "confluent.topic.bootstrap.servers": "kafka-2-0:9092,kafka-2-1:9092,kafka-2-2:9092",
    "consumer.override.bootstrap.servers":"kafka-2-0:9092,kafka-2-1:9092,kafka-2-2:9092",
    "confluent.topic.replication.factor": "1"
    }' | jq

# Коннектор для зеркалирования кластера
curl -X PUT http://localhost:8083/connectors/mirror2/config \
  -H "Content-Type: application/json" \
  --data '{
      "name": "mirror2",
      "connector.class": "org.apache.kafka.connect.mirror.MirrorSourceConnector",
      "source.cluster.alias":"source",
      "target.cluster.alias": "target",
      "topics":"app-.*",
      "source.cluster.bootstrap.servers":"kafka-1-0:9092",
      "target.cluster.bootstrap.servers":"kafka-2-0:9092",
      "key.converter": "org.apache.kafka.connect.converters.ByteArrayConverter",
      "value.converter": "org.apache.kafka.connect.converters.ByteArrayConverter",
      "producer.override.bootstrap.servers":"kafka-2-0:9092",
      "offset-syncs.topic.replication.factor":"1"
      }' | jq