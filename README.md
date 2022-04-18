#DOCUMENTATION

Exchange Rate Service is a Rest API, which serves reference rates, converts amount from one currency to another, shows interactive charts for a currency pair

##User Stories
As a user, who accesses this service through a user interface, ...
1. I want to retrieve the ECB reference rate for a currency pair, e.g. USD/EUR or
   HUF/EUR.
2. I want to retrieve an exchange rate for other pairs, e.g. HUF/USD.
3. I want to retrieve a list of supported currencies and see how many times they were
   requested.
4. I want to convert an amount in a given currency to another, e.g. 15 EUR = ??? GBP
5. I want to retrieve a link to a public website showing an interactive chart for a given
   currency pair.

##How to run the project

### Using Docker
- gradle build
- docker build -t exchange-rate .
- docker run -m1024M --cpus 2 -it -p 8080:8080 --rm exchange-rate

### locally
- gradle build
- gradle bootRun

### Endpoints
- POST: http://localhost:8080/exchange/reference
- POST: http://localhost:8080/exchange/convert
- GET: http://localhost:8080/exchange/supportedCurrencies
- POST: http://localhost:8080/exchange/chart

### Run Tests
- gradle test -i

### Scripts
- Run [Get rate script](scripts/getRate.sh) to get the exchange rate for a currency
- Run [Get supported currencies](scripts/getSupportedCurrencies.sh) to get all supported currencies, and how many times they are requested
- Run [Convert currency](scripts/convertCurrency.sh) to convert an amount from one currency to another

## Swagger UI
http://localhost:8080/swagger-ui/index.html#/