# Goodrec

## General info
Goodrec is web application which allows you to add recipes!

Current features:
- Create, get, update, delete recipes
- Support for inserting images to recipe
- API security (JWT tokens)

## Frameworks and libraries
* Spring boot, data
* JWT, Java JWT library
* Flapdoodle - embedded mongodb for testing/development

MongoDB as database

## Build and run
Clone project into your workspace:

``` git clone https://github.com/tronez/goodrec.git```

### dev profile:

Run terminal, change to root directory of project and enter:

```gradlew bootRun --args='--spring.profiles.active=dev'```

## Status
version 1.0 _finished_

## Swagger API documentation
```
http://localhost:8080/swagger-ui.html
```