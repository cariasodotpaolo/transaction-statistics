
Build Commands:

1. Build the application

   > mvn clean install -P local | dev | uat (default is local)

2. See if the Spring Boot application runs:

   > mvn spring-boot:run -P local | dev | uat (default is local)

   Note: spring-boot maven plugin will do a clean build

3. To run maven build with integration tests:

   > mvn clean install -P integration


API:

http://localhost:8081/txn-stats/transaction

http://localhost:8081/txn-stats/statistics

http://localhost:8081/txn-stats/statistics/{seconds}
