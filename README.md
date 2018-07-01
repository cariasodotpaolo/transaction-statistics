
Build Commands:

1. Build the application

   > mvn clean install -P local | dev | uat (default is local)

2. See if the Spring Boot application runs:

   > mvn spring-boot:run -P local | dev | uat (default is local)

   Note: spring-boot maven plugin will do a clean build

3. To run maven build with integration tests:

   > mvn clean install -P integration


API:

POST http://localhost:8081/txn-stats/transaction

Request body:
```
{
    "amount": 1.87,
    "timestamp": 1530423160297
}
```

--------------------------------------------

GET http://localhost:8081/txn-stats/statistics (Returns statistics for last minute/ 60 seconds )
GET http://localhost:8081/txn-stats/statistics/{seconds} (Returns statistics for user-defined interval in seconds)

Response body:
```
{
    "sum": 50.82,
    "avg": 1.54,
    "max": 1.96,
    "min": 1.03,
    "count": 33
}
```