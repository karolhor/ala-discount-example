# Discount Service
The purpose of this repository is to present an example of the Spring Boot app with Kotlin. 
Below is the list of used technologies:
* `Kotlin 2.1+`
* `Spring Boot 3.4+`
* `Gradle 8.13`
* `PostgreSQL 17+`


# Requirements

To run the app you need:
* `JVM 21+` - to build the app
* `Docker` - to run app in a container
* `Docker Compose` - tu run demo with all dependencies

# Test
The Integration and Unit tests are written using the following:
* [JUnit 5](https://junit.org/junit5/)
* [assertk](https://github.com/willowtreeapps/assertk)
* [MockK](https://mockk.io/)

Execute this command for running all tests:
```shell
./gradlew test
```

# Build and run

First, you need to compile and build the `jar` file. This is as simple as running this command:

```shell
./gradlew build
```

To run the demo app with all dependencies, you must run `docker-compose` command.

```shell
docker-compose up
```

Note: If you're using newer Docker, you probably have [Docker Compose V2](https://docs.docker.com/compose/releases/migrate/). 
If yes, then this command will work for you: 
```shell
docker compose up
```

The App server is available at http://localhost:8080.

# API

## Product Info

### Request

`GET /v1/products/{id}`

    curl -i  http://localhost:8080/v1/products/1c8a9b2d-3e4f-5a6b-7c8d-9e0f1a2b3c4d

### Response

    HTTP/1.1 200 OK
    Content-Length: 208
    Content-Type: application/json
    
    {
        "description": "Lightweight and powerful laptop with Apple M2 chip, 13-inch Retina display, and long battery life.",
        "id": "1c8a9b2d-3e4f-5a6b-7c8d-9e0f1a2b3c4d",
        "name": "Apple MacBook Air M2",
        "price": "4799.00"
    }

## Get the total price for productId and quantity
Note: When multiple discounts for product are available then the one with the highest amount wins.  

### Request

`GET /v1/products/{id}/total-price?quantity={quantity}`

    curl -i  http://localhost:8080/v1/products/1c8a9b2d-3e4f-5a6b-7c8d-9e0f1a2b3c4d/total-price?quantity=10

### Response

    HTTP/1.1 200 OK
    Content-Length: 185
    Content-Type: application/json
    
    {
        "discount": {
            "amount": "719.85",
            "rate": "1.50"
        },
        "productId": "1c8a9b2d-3e4f-5a6b-7c8d-9e0f1a2b3c4d",
        "quantity": 10,
        "totalPrice": {
            "base": "47990.00",
            "final": "47270.15"
        },
        "unitPrice": "4799.00"
    }

| field              | description                                     |
|--------------------|-------------------------------------------------|
| `discount.amount`  | The value of discount amount for given quantity |
| `discount.rate`    | The value of discount e.g. 1.5%                 |
| `totalPrice.base`  | The price for order before discount             |
| `totalPrice.final` | The price for order after discount              |
| `unitPrice`        | Base product price                              |

# Example products

* `f9e8d7c6-b5a4-3210-fedc-ba9876543210` - no discount
* `98765432-fedc-ba98-7654-3210abcdef01` - fixed 7% of total price discount
* `a0b1c2d3-e4f5-6789-0abc-def012345678` - quantity discount. Rates:
  * 1-9 units: No discount
  * 10-19 units: 5% discount
  * 20-49 units: 10% discount
  * 50+ units: 15% discount
* `1c8a9b2d-3e4f-5a6b-7c8d-9e0f1a2b3c4d` - mixed discount (fixed + quantity)
  * fixed 1.5% of total price discount
  * 15-39 units: 5% discount
  * 40+ units: 8%
