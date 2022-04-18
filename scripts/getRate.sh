#!/bin/bash


if [ -z "$1" ]
 then
    echo "No argument supplied"
    echo "please specify reference you want to get"
    echo "example: ./getRate.sh EUR/TRY"
    exit 1
fi

curl  --header "Content-Type: application/json" \
      --request POST \
      -d "$1" \
      http://localhost:8080/exchange/reference \
      | jq
#If you don't have jq installed at your pc, please comment the line above