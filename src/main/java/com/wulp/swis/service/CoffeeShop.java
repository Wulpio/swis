package com.wulp.swis.service;

import com.wulp.swis.domain.Product;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class CoffeeShop {

    private final List<Product> availableProducts = initProducts();

    public List<Product> getAvailableProducts() {
        return availableProducts;
    }

    public String createReceipt(Product... order) {
        if (order.length == 0) {
            throw new IllegalArgumentException("Cannot create a receipt from empty order");
        }
        Arrays.stream(order).forEach(e -> {
            if (!availableProducts.contains(e)) {
                throw new IllegalArgumentException("Product is not supported");
            }
        });

        var receipt = new StringBuilder("Receipt\n");
        for (Product product : order) {
            String format = String.format("%40s %20.2f CHF\n", product.getName(), product.getPrice());
            receipt.append(format);
        }

        var sumPrice = Arrays.stream(order).map(Product::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

        receipt.append("---------------------------------------------\n");
        receipt.append(String.format("%40s %20.2f CHF\n", "Sum", sumPrice.doubleValue()));

        return receipt.toString();
    }

    private List<Product> initProducts() {
        return Arrays.asList(
                new Product("small coffee", "2.50"),
                new Product("medium coffee", "3.00"),
                new Product("large coffee", "3.50"),
                new Product("Bacon Roll", "4.50"),
                new Product("Freshly squeezed orange juice (0.25l)", "3.95"),
                new Product("Extra milk", "0.30"),
                new Product("Foamed milk", "0.50"),
                new Product("Special roast coffee", "0.90")
        );
    }
}
