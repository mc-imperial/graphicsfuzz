#!/bin/bash
set -x
set -e
set -u

docker build -t fuzzer-ci-image .
