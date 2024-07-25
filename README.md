# Exchange Rate And Conversion Service

This project is a Exchange Rate And Conversion Service implemented in Java using Spring Boot and Gradle. 
It provides a set of RESTFul APIs to get exchange rate, convert currency and history of conversions.

The data is stored in the `TRANSACTION` table in an H2 database.<br>
For performance optimization index `idx_transaction_date` on `TRANSACTION` table created.

## Conversion Controller API Endpoints

The `ConversionController` class manages operations related to currency conversion in the system.<br>
It integrates with the `ConversionService` to perform business logic operations and manages HTTP requests and responses for the following endpoints:

### POST /api/conversion

Creates a new conversion transaction.

**Request Body:**

```json
{
  "sourceCurrency" : "USD",
  "targetCurrency" : "TRY",
  "sourceAmount" : 2
}
```

**Response:**

```json
{
  "transactionId": "ccb18d81-9e78-4f05-890e-623b7a143715",
  "convertedAmount": 66.06422
}
```

### GET /api/conversion/history

Lists conversion transactions. This endpoint supports filtering by conversion date and transaction id, and also supports pagination.

**Query Parameters:**

- `conversionDate` (optional): The date of the conversion transaction in ISO format (YYYY-MM-DD).
- `transactionId` (optional): The id of the conversion transaction.
- `pageNo` (default 0): The page number for pagination.
- `pageSize` (default 20): The number of items per page for pagination.

**Request:**
```http
GET /api/conversion/history?conversionDate=2024-07-25&pageNo=0&pageSize=20
```

**Response:**

```json
{
  "conversionTransactionList": [
    {
      "id": "1694e6ef-d9d5-4c9f-999b-51591b17159e",
      "convertedAmount": 66.11,
      "sourceCurrency": "USD",
      "targetCurrency": "TRY",
      "amount": 2.00,
      "transactionDate": "2024-07-25"
    },
    {
      "id": "f4f203c4-8504-4765-b1aa-e13cc7f50d6d",
      "convertedAmount": 66.06,
      "sourceCurrency": "USD",
      "targetCurrency": "TRY",
      "amount": 2.00,
      "transactionDate": "2024-07-25"
    },
    {
      "id": "a2f14f58-8f98-43e6-bd05-2fe63d7fc781",
      "convertedAmount": 66.06,
      "sourceCurrency": "USD",
      "targetCurrency": "TRY",
      "amount": 2.00,
      "transactionDate": "2024-07-25"
    },
    {
      "id": "70587e4d-3abe-4d65-96c9-d8d1a3c296e4",
      "convertedAmount": 66.06,
      "sourceCurrency": "USD",
      "targetCurrency": "TRY",
      "amount": 2.00,
      "transactionDate": "2024-07-25"
    },
    {
      "id": "7b07f64b-038a-4485-b839-125aeec1448b",
      "convertedAmount": 66.06,
      "sourceCurrency": "USD",
      "targetCurrency": "TRY",
      "amount": 2.00,
      "transactionDate": "2024-07-25"
    },
    {
      "id": "ccb18d81-9e78-4f05-890e-623b7a143715",
      "convertedAmount": 66.06,
      "sourceCurrency": "USD",
      "targetCurrency": "TRY",
      "amount": 2.00,
      "transactionDate": "2024-07-25"
    }
  ],
  "totalPage": 1,
  "totalElements": 6
}
```

## ExchangeRate Controller API Endpoints

The `ExchangeRateController` class manages operations related to exchange rate in the system.<br>
It integrates with the `ExchangeRateService` to perform business logic operations and handles HTTP requests and responses for the following endpoints:

### GET /api/exchange-rate

Gets the exchange rate between two currencies.

**Query Parameters:**

- `baseCurrency`: The base currency code.
- `targetCurrency`: The target currency code.

**Request:**
```http
GET /api/exchange-rate?baseCurrency=GBP&targetCurrency=TRY
```

**Response:**

```json
{
  "rate": 42.512532
}
```
## Installation

To build and run the service, use the following command:

```bash
docker compose up --build
```

## API Documentation

You can access the Swagger API documentation and usage at the following URL:

```
http://localhost:8080/swagger-ui/index.html
```

## H2 Database

The jdbc url, username and password can be found in the `application.properties` file.<br>
You can log in to the H2 database console at the following URL:
```
http://localhost:8080/h2-console/login.do