#!/bin/bash
set -x
set -e
set -u

docker stop fuzzer-ci-container | true
docker rm fuzzer-ci-container | true
docker create -it --name fuzzer-ci-container --restart always fuzzer-ci-image
