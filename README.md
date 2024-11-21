# Race application command service

# Configuration and Installation

1. Setup development environment via docker compose if you don't do that with race-application-query-service microservice
```
version: '3.9'

services:
  rabbitmq:
    image: rabbitmq:management
    container_name: rabbitmq
    ports:
      - "5672:5672" # RabbitMQ broker port
      - "15672:15672" # RabbitMQ management console
    environment:
      RABBITMQ_DEFAULT_USER: guest
      RABBITMQ_DEFAULT_PASS: guest

  postgres:
    image: postgres:15
    container_name: postgres
    ports:
      - "5432:5432"
    environment:
      POSTGRES_USER: race_user
      POSTGRES_PASSWORD: race_password
    volumes:
      - postgres_data:/var/lib/postgresql/data

volumes:
  postgres_data:

```

2. Configure app **before** building:

   The basic configuration is defined in [application.properties](src/main/resources/application.properties):
    - You _must_ create application-local.properties file and configure all properties from [application-local-template.properties](src/main/resources/application-local-template.properties) file


3. Build app:

   To compile app simply use Maven. A runnable `jar` file containing the application and configuration will be created in the subdirectory `/target`.
   For windows run following command
    ```
    # .\mvnw.cmd package -DskipTests
    ```

4. Run app:

   To start the application run (please do not run app as root):

    ```
    # java -jar .\target\race-application-command-service-0.0.1-SNAPSHOT.jar --spring.profiles.active=local
    ```
   