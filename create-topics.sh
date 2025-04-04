#!/bin/bash

# топик для сырых данных о товарах
kafka-topics.sh --create --topic app-raw-goods --partitions 3 --replication-factor 3 --bootstrap-server localhost:9094
# Топик для отфильтрованных то варов
kafka-topics.sh --create --topic app-filtered-goods --partitions 3 --replication-factor 3 --bootstrap-server localhost:9094
# Топик для хранения черного списка товаров
kafka-topics.sh --create --topic app-blacklist-goods --partitions 3 --replication-factor 3 --bootstrap-server localhost:9094
# топик для хранения истории поисковых запросов
kafka-topics.sh --create --topic app-search-queries --partitions 3 --replication-factor 3 --bootstrap-server localhost:9094

# Топик для рекомендаций
kafka-topics.sh --create --topic recommendations --partitions 3 --replication-factor 3 --bootstrap-server localhost:9097
