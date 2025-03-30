#!/bin/bash

kafka-topics.sh --create --topic app-raw-goods --partitions 3 --replication-factor 3 --bootstrap-server localhost:9094
kafka-topics.sh --create --topic app-filtered-goods --partitions 3 --replication-factor 3 --bootstrap-server localhost:9094
kafka-topics.sh --create --topic app-blacklist-goods --partitions 3 --replication-factor 3 --bootstrap-server localhost:9094
kafka-topics.sh --create --topic app-search-queries --partitions 3 --replication-factor 3 --bootstrap-server localhost:9094

kafka-topics.sh --create --topic recommendations --partitions 3 --replication-factor 3 --bootstrap-server localhost:9097
