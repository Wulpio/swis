package com.wulp.swis.service;

import com.wulp.swis.domain.Product;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CoffeeShopTest {

    @Test
    void GIVEN_order_without_any_products_WHEN_createReceipt_THEN_exception_is_returned() {
        //GIVEN
        CoffeeShop cs = new CoffeeShop();
        Product[] products = new Product[0];

        //WHEN
        var thrown = assertThrows(
                IllegalArgumentException.class,
                () -> cs.createReceipt(products)
        );

        //THEN
        assertEquals("Cannot create a receipt from empty order", thrown.getMessage());
    }

    @Test
    void GIVEN_order_with_non_supported_product_WHEN_createReceipt_THEN_exception_is_returned() {
        //GIVEN
        var cs = new CoffeeShop();
        var product = new Product("bla", "123.45");

        //WHEN
        var thrown = assertThrows(
                IllegalArgumentException.class,
                () -> cs.createReceipt(product)
        );

        //THEN
        assertEquals("Product is not supported", thrown.getMessage());
    }

    @Test
    void GIVEN_order_with_one_product_WHEN_createReceipt_THEN_receipt_with_one_product_is_returned() {
        //GIVEN
        var cs = new CoffeeShop();

        //WHEN
        var receipt = cs.createReceipt(cs.getAvailableProducts().get(0));

        //THEN
        assertEquals("""
                Receipt
                                            small coffee                 2.50 CHF
                ---------------------------------------------
                                                     Sum                 2.50 CHF
                                """, receipt);
    }

    @Test
    void GIVEN_order_with_three_product_WHEN_createReceipt_THEN_receipt_with_one_product_is_returned() {
        //GIVEN
        var cs = new CoffeeShop();

        //WHEN
        var receipt = cs.createReceipt(
                cs.getAvailableProducts().get(0),
                cs.getAvailableProducts().get(2),
                cs.getAvailableProducts().get(4));

        //THEN
        assertEquals("""
                Receipt
                                            small coffee                 2.50 CHF
                                            large coffee                 3.50 CHF
                   Freshly squeezed orange juice (0.25l)                 3.95 CHF
                ---------------------------------------------
                                                     Sum                 9.95 CHF
                                """, receipt);
    }
}