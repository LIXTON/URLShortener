# URLShortener
Mini Java Project for Redirecting and Storing Urls Shortcuts

## Requirements
- Apache Maven 3.6.3
- Java 1.8.0_111

## Specifications
- Spark Framework
- Hibernate
- H2 DB
- Gson

## Dexcription
This project is a small Rest Service using Java with Spark Framework and an H2 in-memory DB.
It consist of 3 end points:
- GET /list
- GET /<alias>
- POST /newUrl


  ### /list
  It returns all the urls stored with their respective alias stored.
  Note: Still has issues to display

  ### /:alias
  The site will redirect you to the url associated with that alias if any.
  Otherwise it will display a proper JSON output

  ### /newUrl
  The site is expecting a JSON request with an "url" parameter. Other things will throw an error 500.
  With the url provided the site will generate and display the alias. The conditions for the alias are:
  - if the url contains **google** the alias will be a random aplhabetic with length of 5
  - if the url contains **yahoo** the alias will be a random alphanumeric with length of 7
  - any other url will contain only consonants, excluding the https or http
  
  Of course the same url will output the same alias

## Run Project
To run the project be sure to build first:
```
mvn package
```
After building execute it:
```
java -cp target/URLShortener-1.0-SNAPSHOT.jar com.lxn.urlshortener.Application
```

Note: The project is running at the port **8082**
