#!/bin/bash

curl  -v --header "Content-Type: application/json" \
      --request GET \
      http://localhost:8080/exchange/supportedCurrencies \
      | jq
#If you don't have jq installed at your pc, please comment the line above