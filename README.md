# Checkout Component 3.0
by [Filip Cyboran](https://github.com/cyboranf)
please read ['My Idea'](#My-Idea) section

## [Instruction How to Fast run a project](#how-to-run)   <--- Click here to jump to the instruction

# My Idea

I imagine going to a wholesale store like Makro. To start shopping, I first need to register as a client (`POST /api/auth/beClient`) to get a Client card. Once registered, I can put Client card to a Cart and if I authenticate (`POST /api/auth/takeCart`) and receive a Cart that allows me to go shopping and add items to a Cart.

Once authenticated, I can:
- Add items to my cart using `POST /api/cart/addItem`.
- The cart applies two kinds of promotions:
    - Multi-priced promotions: Buy N of an item and pay a special reduced price.
    - Bundle discounts: Buy item X together with item Y and save Z cents.

While adding items to the cart, I can always see how many items I have, how promotions apply, and how much I save. For example, after adding some items, I might see a JSON response like this:

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

After I'm done adding items, I finalize the purchase (POST /api/cart/finalizePurchase) and receive a detailed receipt showing all the items, their normal and discounted prices, and the total savings:

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

## Overview

This project implements a market checkout component with a clean, RESTful API. It calculates the total singleNormalPrice of items in a shopping cart, supports multi-priced discounts, bundling discounts, and receipt generation. User authentication with JWT ensures secure access, and the service is designed for scalability and ease of deployment.

## How to Run

