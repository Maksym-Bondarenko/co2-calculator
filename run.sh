#!/usr/bin/env bash
set -a          # export everything that follows
source env
set +a
java -cp target/classes org.challange.Main "$@"