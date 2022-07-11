## Recepies Application
- This is a standalone java application which helps users to manage their favourite recepies.

## Prerequisites :
- docker
- mongoDB
- mongo-express

## To run application:
- Download/Clone the repository
- Install java, docker
- Run `docker-compose up` to bring up the mongo db
- Run `mvn clean package` to build the package
- Run `java -jar target/recepies-0.0.1-SNAPSHOT.jar`

## server
- localhost:8080/swagger-ui.html#/  ->  *it includes GUI for using api's*
- localhost:8080/api

## Operations allowed :
- **GET**       -> Get list of recepies that in database
    - *Search*   -> Search by one or many parameters for recepies
- **POST**      -> Create new recepie
- **PUT**       -> Update recepie
- **DELETE**    -> Delete a recepie by ID
*you can give multiple strings to include or exclude from ingredients or instructions*

## Database
Database is MongoDB
- Username : you can change it in `application.properties` *Default is `rootuser`*
- Password : you can change it in `application.properties` *Default is `rootpass`*
- default port `27017`


*Use postman or swagger url to GET, POST, PUT and DELETE*

## Tests
- Unit test are provided in src/test/java/com/recepie/service/ServiceTest.java. The connection to DB is mocked and tested the logic here
- Integration tests are provided in src/test/java/com/recepie/component/ComponentTest.java. Here we connect to DB and create a record and read the record and delete the record from REST endpoint.

## Improvements
- Integrate the Apache Lucene(or any similar library) to search the real world text search like "Recipes without salmon as an ingredient made using oven".
- Add api authentication and authorization
- Add more unit test
- Need to remove integration test case to different directory and automate its running after unit test in mvn build.s
- Dockerize java application
