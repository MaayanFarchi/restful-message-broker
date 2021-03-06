# Restful Message Broker Exercise

# Requirements
MAVEN, 
JAVA 11 

# APIS 
1) subscribe to topic
GET http://localhost:8080/subscribe/{topicId}
headers - client-id
2) publish to topic
POST http://localhost:8080/publish/{topicId}
3) unsubscribe from topic
DELETE http://localhost:8080/subscribe/{topicId}
headers - client-id
41) listen to topic
GET http://localhost:8080/consumer/{topicId}
headers - client-id

# RUN application from CMD 
1) Go to project root (where POM.xml is) 
2) Run 
mvn clean install
3) Run 
mvn spring-boot:run

# Run applcation from IDE 
1) Go to File > New > Project from existing source 
2) Chose project pom.xml 
3) Run application 


