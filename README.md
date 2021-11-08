## REQUIREMENTS
- Java 11
- Maven
- Docker [optional]

## BUILD&TEST
mvn clean install

## BUILD DOCKER IMAGE [OPTIONAL]
Assure that docker daemon is running before launching below command:  
mvn spring-boot:build-image -Dspring-boot.build-image.imageName=tui-backend-test-pswida

## RUN
- Using maven:  
  mvn spring-boot:run
- Using docker [optional]:  
  docker run -p 8090:8090 -t tui-backend-test-pswida

## PILOTES REST API
Swagger API available after starting application at: http://localhost:8090/swagger-ui/#/

## ASSUMPTIONS 
TODO

