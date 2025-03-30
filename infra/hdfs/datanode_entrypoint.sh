#!/usr/bin/env bash

set -e

mkdir -p /usr/local/hadoop/hdfs/datanode
echo "Directory created!"
chmod -R 777 /usr/local/hadoop/hdfs/datanode
echo "chmod -R 777"

exec "$@"