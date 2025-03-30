#!/bin/bash

KSQLDB_URL="http://localhost:8088"

send_ksql_query() {
    local query="$1"
    curl -X POST "${KSQLDB_URL}/ksql" \
         -H "Content-Type: application/vnd.ksql.v1+json; charset=utf-8" \
         -d @- << EOF
{
  "ksql": "$query",
  "streamsProperties": {}
}
EOF
    echo ""
}

send_ksql_query "CREATE STREAM recommendations_stream (
    username STRING,
    product_name STRING
) WITH (
    KAFKA_TOPIC = 'recommendations',
    VALUE_FORMAT = 'JSON',
    PARTITIONS = 3
);"

send_ksql_query "CREATE TABLE user_recommendations AS
 SELECT username,
       COLLECT_SET(product_name) AS recommended_products
 FROM recommendations_stream
 GROUP BY username
 EMIT CHANGES;"
