# Nagarro Java Task
Account Statements

# Technologies
* **Back-End**

   Spring Boot (Security 'JWT authentication' - JPA - test)
* **Front-End**
* 
  HTML, CSS, Bootstrap and jQuery.


## Application Notes
* **Server Port**: 8080
* **Token Expiration Time is 5 minutes**

## Steps To Run The Application
 1. clone git repo or download the source code.
 2. setup java IDE (ex. Intellji).
 3. import application to the IDE, and download dependencies.
 4. update application.properties
    * *datasource.url* this property should have the path of MS access file
    * *logging.file.name* this property should have loggin path and log file name.
 5. run the applictaion
 6. start working with rest APIs

## Application Roles
  * **ADMIN** user with this role can ethier search for statements by account ID and filter parameters (date & amount) or by account ID only.
 * **USER** uer with this role can ONLY search for statements by account ID.

## Endpoints
 * ***login API*** take username and password and retrieve JWT token to be use in further requests.
```
POST http://localhost:8080/login
```
* ***logout API*** take no parameters and use to end user session
```
GET http://localhost:8080/logOut
```
* ***Search API for Statements by account ID*** take account ID and retrieve the corresponded statements
```
GET http://localhost:8080/api/statement/search-accountId/?{account-Id}
```
* ***Search API for Statements by account ID with filter params or without*** take account ID with (date & amount) and retrieve the corresponded statements
```
GET http://localhost:8080/api/statement/search/
```
## 
