package com.wulp.swis.service;

import com.wulp.swis.domain.Product;
import com.wulp.swis.domain.ProductType;
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
                () -> cs.createReceipt(1L, products).generateReceipt()
        );

        //THEN
        assertEquals("Cannot create a receipt from empty order", thrown.getMessage());
    }

    @Test
    void GIVEN_order_with_non_supported_product_WHEN_createReceipt_THEN_exception_is_returned() {
        //GIVEN
        var cs = new CoffeeShop();
        var product = new Product("bla", "123.45", ProductType.BEVERAGE);

        //WHEN
        var thrown = assertThrows(
                IllegalArgumentException.class,
                () -> cs.createReceipt(1L, product).generateReceipt()
        );

        //THEN
        assertEquals("Product is not supported", thrown.getMessage());
    }

    @Test
    void GIVEN_order_with_one_product_WHEN_createReceipt_THEN_receipt_with_one_product_is_returned() {
        //GIVEN
        var cs = new CoffeeShop();

        //WHEN
        var receipt = cs.createReceipt(1L, cs.getAvailableProducts().get(0)).generateReceipt();

        //THEN
        assertEquals("""
                Receipt for customer: 1
                                            small coffee                 2.50 CHF
                ------------------------------------------------------------------------------------------
                                                     Sum                 2.50 CHF
                                """, receipt);
    }

    @Test
    void GIVEN_order_with_3_product_WHEN_createReceipt_THEN_receipt_with_one_product_is_returned() {
        //GIVEN
        var cs = new CoffeeShop();

        //WHEN
        var receipt = cs.createReceipt(1L,
                cs.getAvailableProducts().get(0),
                cs.getAvailableProducts().get(2),
                cs.getAvailableProducts().get(4))
                .generateReceipt();

        //THEN
        assertEquals("""
                Receipt for customer: 1
                                            small coffee                 2.50 CHF
                                            large coffee                 3.50 CHF
                   Freshly squeezed orange juice (0.25l)                 3.95 CHF
                ------------------------------------------------------------------------------------------
                                                     Sum                 9.95 CHF
                                """, receipt);
    }

    @Test
    void GIVEN_two_separate_orders_with_3_product_each_WHEN_createReceipt_THEN_second_receipt_have_one_free_product() {
        //GIVEN
        var cs = new CoffeeShop();

        //WHEN
        var receipt = cs.createReceipt(1L,
                cs.getAvailableProducts().get(0),
                cs.getAvailableProducts().get(2),
                cs.getAvailableProducts().get(4))
                .generateReceipt();

        //THEN
        assertEquals("""
                Receipt for customer: 1
                                            small coffee                 2.50 CHF
                                            large coffee                 3.50 CHF
                   Freshly squeezed orange juice (0.25l)                 3.95 CHF
                ------------------------------------------------------------------------------------------
                                                     Sum                 9.95 CHF
                                """, receipt);

        //WHEN
        var receipt2 = cs.createReceipt(1L,
                cs.getAvailableProducts().get(0),
                cs.getAvailableProducts().get(2),
                cs.getAvailableProducts().get(4))
                .generateReceipt();

        //THEN
        assertEquals("""
                Receipt for customer: 1
                                            small coffee                 2.50 CHF
                                            large coffee                 0.00 CHF
                   Freshly squeezed orange juice (0.25l)                 3.95 CHF
                ------------------------------------------------------------------------------------------
                                                     Sum                 6.45 CHF
                                                """, receipt2);
    }

    @Test
    void GIVEN_two_separate_orders_with_3_product_each_for_2_customer_WHEN_createReceipt_THEN_no_one_has_free_product() {
        //GIVEN
        var cs = new CoffeeShop();

        //WHEN
        var receipt = cs.createReceipt(1L,
                cs.getAvailableProducts().get(0),
                cs.getAvailableProducts().get(2),
                cs.getAvailableProducts().get(4))
                .generateReceipt();

        //THEN
        assertEquals("""
                Receipt for customer: 1
                                            small coffee                 2.50 CHF
                                            large coffee                 3.50 CHF
                   Freshly squeezed orange juice (0.25l)                 3.95 CHF
                ------------------------------------------------------------------------------------------
                                                     Sum                 9.95 CHF
                                """, receipt);

        //WHEN
        var receipt2 = cs.createReceipt(2L,
                cs.getAvailableProducts().get(0),
                cs.getAvailableProducts().get(2),
                cs.getAvailableProducts().get(4))
                .generateReceipt();

        //THEN
        assertEquals("""
                Receipt for customer: 2
                                            small coffee                 2.50 CHF
                                            large coffee                 3.50 CHF
                   Freshly squeezed orange juice (0.25l)                 3.95 CHF
                ------------------------------------------------------------------------------------------
                                                     Sum                 9.95 CHF
                                                                """, receipt2);
    }

    @Test
    void GIVEN_tree_separate_orders_with_3_product_each_for_2_customer_WHEN_createReceipt_THEN_customer_with_2_orders_have_one_free_product() {
        //GIVEN
        var cs = new CoffeeShop();

        //WHEN
        var receipt = cs.createReceipt(1L,
                cs.getAvailableProducts().get(0),
                cs.getAvailableProducts().get(2),
                cs.getAvailableProducts().get(4))
                .generateReceipt();

        //THEN
        assertEquals("""
                Receipt for customer: 1
                                            small coffee                 2.50 CHF
                                            large coffee                 3.50 CHF
                   Freshly squeezed orange juice (0.25l)                 3.95 CHF
                ------------------------------------------------------------------------------------------
                                                     Sum                 9.95 CHF
                                """, receipt);

        var receipt2 = cs.createReceipt(2L,
                cs.getAvailableProducts().get(0),
                cs.getAvailableProducts().get(2),
                cs.getAvailableProducts().get(4))
                .generateReceipt();

        assertEquals("""
                Receipt for customer: 2
                                            small coffee                 2.50 CHF
                                            large coffee                 3.50 CHF
                   Freshly squeezed orange juice (0.25l)                 3.95 CHF
                ------------------------------------------------------------------------------------------
                                                     Sum                 9.95 CHF
                                                                """, receipt2);
        //WHEN
        var receipt3 = cs.createReceipt(1L,
                cs.getAvailableProducts().get(0),
                cs.getAvailableProducts().get(2),
                cs.getAvailableProducts().get(4))
                .generateReceipt();

        //THEN
        assertEquals("""
                Receipt for customer: 1
                                            small coffee                 2.50 CHF
                                            large coffee                 0.00 CHF
                   Freshly squeezed orange juice (0.25l)                 3.95 CHF
                ------------------------------------------------------------------------------------------
                                                     Sum                 6.45 CHF
                                                                                """, receipt3);
    }

    @Test
    void GIVEN_two_separate_orders_with_4_product_each_with_one_SNACK_WHEN_createReceipt_THEN_second_receipt_have_one_free_product() {
        //GIVEN
        var cs = new CoffeeShop();

        var receipt = cs.createReceipt(1L,
                cs.getAvailableProducts().get(0),
                cs.getAvailableProducts().get(2),
                cs.getAvailableProducts().get(1),
                cs.getAvailableProducts().get(4))
                .generateReceipt();

        assertEquals("""
                Receipt for customer: 1
                                            small coffee                 2.50 CHF
                                            large coffee                 3.50 CHF
                                           medium coffee                 3.00 CHF
                   Freshly squeezed orange juice (0.25l)                 3.95 CHF
                ------------------------------------------------------------------------------------------
                                                     Sum                12.95 CHF
                                                """, receipt);

        //WHEN
        var receipt2 = cs.createReceipt(1L,
                cs.getAvailableProducts().get(0),
                cs.getAvailableProducts().get(1),
                cs.getAvailableProducts().get(2),
                cs.getAvailableProducts().get(4))
                .generateReceipt();

        //THEN
        assertEquals("""
                Receipt for customer: 1
                                            small coffee                 0.00 CHF
                                           medium coffee                 3.00 CHF
                                            large coffee                 3.50 CHF
                   Freshly squeezed orange juice (0.25l)                 3.95 CHF
                ------------------------------------------------------------------------------------------
                                                     Sum                10.45 CHF
                                                                """, receipt2);
    }

    @Test
    void GIVEN_order_with_6_beverages_WHEN_createReceipt_THEN_receipt_with_fifth_free_beverage() {
        //GIVEN
        var cs = new CoffeeShop();

        //WHEN
        var receipt = cs.createReceipt(1L,
                cs.getAvailableProducts().get(0),
                cs.getAvailableProducts().get(0),
                cs.getAvailableProducts().get(0),
                cs.getAvailableProducts().get(1),
                cs.getAvailableProducts().get(1),
                cs.getAvailableProducts().get(1))
                .generateReceipt();

        //THEN
        assertEquals("""
                Receipt for customer: 1
                                            small coffee                 2.50 CHF
                                            small coffee                 2.50 CHF
                                            small coffee                 2.50 CHF
                                           medium coffee                 3.00 CHF
                                           medium coffee                 0.00 CHF
                                           medium coffee                 3.00 CHF
                ------------------------------------------------------------------------------------------
                                                     Sum                13.50 CHF
                                                                """, receipt);
    }

    @Test
    void GIVEN_order_with_12_beverages_WHEN_createReceipt_THEN_receipt_with_fifth_and_tenth_free_beverage() {
        //GIVEN
        var cs = new CoffeeShop();

        //WHEN
        var receipt = cs.createReceipt(1L,
                cs.getAvailableProducts().get(0),
                cs.getAvailableProducts().get(0),
                cs.getAvailableProducts().get(0),
                cs.getAvailableProducts().get(0),
                cs.getAvailableProducts().get(0),
                cs.getAvailableProducts().get(1),
                cs.getAvailableProducts().get(1),
                cs.getAvailableProducts().get(1),
                cs.getAvailableProducts().get(1),
                cs.getAvailableProducts().get(1),
                cs.getAvailableProducts().get(2),
                cs.getAvailableProducts().get(2)
        ).generateReceipt();

        //THEN
        assertEquals("""
                Receipt for customer: 1
                                            small coffee                 2.50 CHF
                                            small coffee                 2.50 CHF
                                            small coffee                 2.50 CHF
                                            small coffee                 2.50 CHF
                                            small coffee                 0.00 CHF
                                           medium coffee                 3.00 CHF
                                           medium coffee                 3.00 CHF
                                           medium coffee                 3.00 CHF
                                           medium coffee                 3.00 CHF
                                           medium coffee                 0.00 CHF
                                            large coffee                 3.50 CHF
                                            large coffee                 3.50 CHF
                ------------------------------------------------------------------------------------------
                                                     Sum                29.00 CHF
                                                                                """, receipt);
    }
}