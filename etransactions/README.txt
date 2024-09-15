Steps to execute a transaction:

1. Create an accounts via a request from Postman:

Request method: POST

Request URL: localhost:8080/api/account/create

Sample request body:

{
    "balance": 50.5,
    "currency": "GBP",
    "iban": "GR7777777777777334777777777"
}

NOTES:

 - The IBAN must start with 'GR' followed by 25 additional digits (Suppose we're conducting transactions between Greek accounts)

 - Also all fields are validated so all fields must be present and currency should have value "GBP"/"USD"/"EUR"


2. Execute a transaction via a request from Postman:

Request method: POST

Request URL: localhost:8080/api/transaction/execute

Sample request body:

{
    "sourceAccountId": "GR7777777777777454777777777",
    "targetAccountId": "GR7777777777777334777777777",
    "amount": 20,
    "currency": "GBP"
}

NOTES:

 - All fields are validated so there must be present and currency should be of type "GBP"


Additional:

All available accounts can be retrieved from the following request:

Request method: GET

Request URL: localhost:8080/api/account/accounts


General Notes:

In order to deploy the app use the maven command:

clean spring-boot:run -V -Dspring-boot.run.fork=false -Dspring.profiles.active=local -Dlogging.config=classpath:log4j.properties

MySql server used is: MySQL57





