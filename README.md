# Pico Bank (micro, nano, pico...)

1. Clone project
```
~ git clone git@github.com:jcsastre/picobank.git
```

2. Maven install and execute
```
~ mvn install
~ java -jar target/picobank-0.0.1-SNAPSHOT.jar
```

3. POST Client
```
~ curl -v -D -X POST -H "Content-Type: application/json" -d '{"email": "email@email.com", "password": "1234"}' http://localhost:8080/clients
...
< Location: http://localhost:8080/clients/6cf0ad9b-115f-4a77-b0fa-664e0b5e2ec5 >
...
```

4. POST Operation
```
~ curl -v -D -X POST -H "Content-Type: application/json" -d '{"operationTypeAsString": "DEPOSIT", "amountInCents": "100"}' http://localhost:8080/clients/6cf0ad9b-115f-4a77-b0fa-664e0b5e2ec5/operations
```

5. GET Client
```
~ curl http://localhost:8080/clients/6cf0ad9b-115f-4a77-b0fa-664e0b5e2ec5
```