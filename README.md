# Sample REST CRUD API with Spring Boot, Mysql, JPA and Hibernate 

## Steps to Setup

**1. Clone the application**

```bash
https://github.com/mhtarora17/BooksApp.git
```


**3. H2 Db is configured in application.properties**

+ open `src/main/resources/application.properties`


**4. Build and run the app using maven**

```bash
mvn package
java -jar target/spring-boot-assignment-mohit-0.0.1-SNAPSHOT.jar

```

Alternatively, you can run the app without packaging it using -

```bash
mvn spring-boot:run
```

The app will start running at <http://localhost:8080>.

## Explore Rest APIs

The app defines following CRUD APIs.

    GET /api/books
    	Url: 
    		http://localhost:8080/api/books
		Input parameters:
		Response: 
			[
    			{
        			"id": 1,
        			"bookName": "Mohit",
        			"authorName": "Mohit",
        			"createdAt": "2020-07-06T03:43:05.229+0000",
        			"createdBy": "Mohit",
        			"updatedAt": "2020-07-06T03:43:05.229+0000",
        			"updatedBy": "Mohit"
    			}    			
    		]
		
    
    POST /api/books
    	Url: 
    		http://localhost:8080/api/book
		Input parameters:
			{
    			"id": 0,
    			"bookName": "Mohit",
    			"authorName": "Mohit",
    			"createdAt": null,
    			"createdBy": "Mohit",
    			"updatedAt": null,
    			"updatedBy": "Mohit"
			}
		Response: 
			{
    			"id": 0,
    			"bookName": "Mohit",
    			"authorName": "Mohit",
    			"createdAt": null,
    			"createdBy": "Mohit",
    			"updatedAt": null,
    			"updatedBy": "Mohit"
			}
    
    PUT /api/book/{bookId}
    	Url: 
    		http://localhost:8080/api/book
		Input parameters:
			bookId
		Body:
			{
    			"id": 0,
    			"bookName": "Mohit",
    			"authorName": "Arora",
    			"createdAt": null,
    			"createdBy": "Mohit",
    			"updatedAt": null,
    			"updatedBy": "Mohit"
			}
		Response: 
			{
    			"id": 0,
    			"bookName": "Mohit",
    			"authorName": "Arora",
    			"createdAt": null,
    			"createdBy": "Mohit",
    			"updatedAt": null,
    			"updatedBy": "Mohit"
			}
    
    DELETE /api/book/{bookId}
    	Input parameters:
			bookId
		Response: 
			{"deleted" : true}

