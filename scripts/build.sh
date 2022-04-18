#!/bin/bash

gradle build
docker build -t exchange-rate .
docker run -m1024M --cpus 2 -it -p 8080:8080 --rm exchange-rate