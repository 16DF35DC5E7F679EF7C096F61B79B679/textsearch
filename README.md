# Getting Started

## Starting an ES server

* Run this command on terminal to start the server
  `docker run -p 9200:9200 -e 'discovery.type=single-node' -e 'http.publish_host=localhost' -e 'http.publish_port=9200' -e
  'xpack.security.enabled=false' docker.elastic.co/elasticsearch/elasticsearch:8.6.2`

* Create an index by hitting this API once the server has started: PUT http://localhost/document

  `{
  "mappings": {
  "properties": {
  "_class": {
  "type": "text",
  "fields": {
  "keyword": {
  "type": "keyword",
  "ignore_above": 256
  }
  }
  },
  "name": {
  "type": "text",
  "fielddata": true
  },
  "id": {
  "type": "keyword"
  },
  "created_at": {
  "type": "date",
  "format": "date_time"
  }
  }
  }
  }`
## Start the spring boot application:
* Create a jar: `mvn clean install -DskipTests`
* `java -jar path/to/your/jarfile.jar fully.qualified.package.Application`
## Run APIs:
* Get all the available documents: GET http://localhost:8082/textsearch/api/v1/document
* Create a document: http://localhost:8082/textsearch/api/v1/document
    * Body: `{
      "name": "<Some sample name>",
      "text": "<Some sample text>"
      }`
* Search for a document: GET http://localhost:8082/textsearch/api/v1/document?keyword=<sample keyword>
* Update a document: PUT http://localhost:8082/textsearch/api/v1/document/<id>
    * Body: `{
      "text": "Some sample text"
      }`
* Delete a document: DELETE http://localhost:8082/textsearch/api/v1/document/<id>

### Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.0.4/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.0.4/maven-plugin/reference/html/#build-image)
* [Spring Data Elasticsearch (Access+Driver)](https://docs.spring.io/spring-boot/docs/3.0.4/reference/htmlsingle/#data.nosql.elasticsearch)
* [Spring Web](https://docs.spring.io/spring-boot/docs/3.0.4/reference/htmlsingle/#web)
* [Spring Data MongoDB](https://docs.spring.io/spring-boot/docs/3.0.4/reference/htmlsingle/#data.nosql.mongodb)

### Guides

The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Accessing Data with MongoDB](https://spring.io/guides/gs/accessing-data-mongodb/)

