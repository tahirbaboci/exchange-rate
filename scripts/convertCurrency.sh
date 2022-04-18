#!/bin/bash


curl  --header "Content-Type: application/json" \
      --request POST \
      -d @data/request.json \
      http://localhost:8080/exchange/convert \
      | jq
#If you don't have jq installed at your pc, please comment the line above