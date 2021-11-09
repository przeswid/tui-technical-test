## REQUIREMENTS
- Java 11
- Maven
- Docker [optional]

## CONFIGURATION
- Initially you can choose between 5,10 and 15 pilotes but feel free to change this configuration in application.properties file (property: pilotes.configuration.availableQuantities)
- Initially the unit price on one pilote is 1.30 Euro but it can be changed in application.properties (property: pilotes.configuration.unitPrice)

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
Swagger API is available after starting application at: http://localhost:8090/swagger-ui/#/  
Default login and password for secured operations are written in application.properties (security.login/security.password)

## ASSUMPTIONS 
For the sake of simplicity:
- Transaction script architecture pattern was used (small project with no further development)
- Http Basic Auth (stateless) was used but in production environment should be implemented more sophisticated solution (JWT)
- We don't know what is a desired Miguel's notification channel so current implementation assumes that OrderReadyToProcessingEventHandler is a place where this channel could be connected (ex. JMS, e-mail, SMS, REST API of Miguel's cooking app ... and so on)
- No I18N was used (but the class Messsages is a good start for I18N)

