# Checkout Component 3.0
by [Filip Cyboran](https://github.com/cyboranf)

## Notes
- Ensure you have Java 17 installed on your machine (`java -version` to check).
- The application runs by default on port `8080`. If this port is already in use, modify the `application.properties` file to change the server port.

## Overview
This project implements a market checkout component with a clean, RESTful API. It calculates the total singleNormalPrice of items in a shopping cart, supports multi-priced discounts, bundling discounts, and receipt generation. User authentication with JWT ensures secure access, and the service is designed for scalability and ease of deployment.
- Note: The most important part of the project is in the 'CartService'

# How to Run

## Clone the Repository
To start, clone the repository from GitHub:
```bash
git clone https://github.com/cyboranf/checkout-component-3.0.git
cd checkout-component-3.0
```

## application.properties

Change these fields
```properties
spring.datasource.username=root
spring.datasource.password=mySQLpassword1.
```


## Run Tests
To verify that everything is working correctly, run the test suite:
```bash
./mvnw test -Dspring.profiles.active=test
```

## Run the Application
To start the application, use the pre-built JAR file included in the repository:
```bash
java -jar target/checkout-0.0.1-SNAPSHOT.jar
```

## Endpoints
Once the application is running, you can access the following endpoints:

### Authentication Endpoints
- **Register a new account**: `POST http://localhost:8080/api/auth/be-client`
  - Example request body:
    ```json
    {
        "login": "your.username",
        "password": "your.password"
    }
    ```
  - Note: The following account is already pre-registered and available for use:
    ```json
    {
        "login": "filip.cyboran",
        "password": "filip.cyboran"
    }
    ```

- **Login and retrieve a JWT token**: `POST http://localhost:8080/api/auth`

### Cart Endpoints
  - Note: They need a JWT token to be passed in the `Authorization` header as a bearer token.
  

- **Add an item to the cart**:
  - `POST http://localhost:8080/api/cart/add-item`
    - with request param: itemId and quantity
    - Or directly via query parameters:
      `POST http://localhost:8080/api/cart/add-item?itemId=3&quantity=5`
    - Example response:
```json
{
    "cart": {
        "cartId": 1,
        "cartItems": [
            {
                "cartItemId": 2,
                "itemId": 2,
                "cartId": 1,
                "quantity": 10,
                "multiPricedPromoDetails": {
                    "singleNormalPrice": 10.0,
                    "singleSpecialPrice": 7.5,
                    "quantityNormalPrice": 0,
                    "quantitySpecialPrice": 10
                }
            },
            {
                "cartItemId": 3,
                "itemId": 3,
                "cartId": 1,
                "quantity": 10,
                "multiPricedPromoDetails": {
                    "singleNormalPrice": 30.0,
                    "singleSpecialPrice": 20.0,
                    "quantityNormalPrice": 2,
                    "quantitySpecialPrice": 8
                }
            },
            {
                "cartItemId": 4,
                "itemId": 1,
                "cartId": 1,
                "quantity": 10,
                "multiPricedPromoDetails": {
                    "singleNormalPrice": 40.0,
                    "singleSpecialPrice": 30.0,
                    "quantityNormalPrice": 1,
                    "quantitySpecialPrice": 9
                }
            },
            {
                "cartItemId": 5,
                "itemId": 4,
                "cartId": 1,
                "quantity": 5,
                "multiPricedPromoDetails": {
                    "singleNormalPrice": 25.0,
                    "singleSpecialPrice": 23.5,
                    "quantityNormalPrice": 1,
                    "quantitySpecialPrice": 4
                }
            }
        ],
        "totalPrice": 713.5,
        "bundleDiscountsPromos": [
            {
                "totalBundlePromoQuantity": 30,
                "totalBundleDiscount": 22.5
            }
        ]
    },
    "success": true,
    "message": "Item added successfully to the cart."
}
```

- **Finalize a purchase**: `POST http://localhost:8080/api/cart/checkout` Don't forget about barer

  - Example response:

```json
{
    "receipt": {
        "receiptId": 2,
        "issuedAt": "19.12.2024 03:36:35",
        "purchasedItems": [
            {
                "itemName": "B",
                "quantity": 10,
                "pricePerOneItem": 10.0,
                "priceBeforeDiscounts": 100.0,
                "priceWithDiscounts": 75.0,
                "itemDiscount": {
                    "bundleDiscountDetails": "No bundle discount",
                    "multiPricedDiscountDetails": "Saved 25,00 due to multi-price promo"
                }
            },
            {
                "itemName": "C",
                "quantity": 10,
                "pricePerOneItem": 30.0,
                "priceBeforeDiscounts": 300.0,
                "priceWithDiscounts": 217.5,
                "itemDiscount": {
                    "bundleDiscountDetails": "Saved 2,50 due to bundle discount on 5 items",
                    "multiPricedDiscountDetails": "Saved 80,00 due to multi-price promo"
                }
            },
            {
                "itemName": "A",
                "quantity": 10,
                "pricePerOneItem": 40.0,
                "priceBeforeDiscounts": 400.0,
                "priceWithDiscounts": 302.0,
                "itemDiscount": {
                    "bundleDiscountDetails": "Saved 8,00 due to bundle discount on 10 items",
                    "multiPricedDiscountDetails": "Saved 90,00 due to multi-price promo"
                }
            },
            {
                "itemName": "D",
                "quantity": 5,
                "pricePerOneItem": 25.0,
                "priceBeforeDiscounts": 125.0,
                "priceWithDiscounts": 119.0,
                "itemDiscount": {
                    "bundleDiscountDetails": "No bundle discount",
                    "multiPricedDiscountDetails": "Saved 6,00 due to multi-price promo"
                }
            }
        ],
        "receiptDiscount": {
            "totalPriceBeforeDiscounts": 925.0,
            "totalDiscount": 211.5,
            "totalPriceWithDiscounts": 713.5
        }
    },
    "success": true,
    "message": "Checkout completed successfully."
}
```

---


