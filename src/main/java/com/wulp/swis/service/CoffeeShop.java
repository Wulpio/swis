package com.wulp.swis.service;

import com.wulp.swis.domain.Product;
import com.wulp.swis.domain.ProductType;
import com.wulp.swis.domain.Receipt;

import java.math.BigDecimal;
import java.util.*;

public class CoffeeShop {

    private static final int LIMIT_FOR_FREE_BEVERAGE = 5;
    private final List<Product> availableProducts = initProducts();
    private final Map<Long, List<Product>> oldOrders = new HashMap<>();

    public List<Product> getAvailableProducts() {
        return availableProducts;
    }

    public Receipt createReceipt(Long customerId, Product... order) {
        assertEmptyProductList(order);
        assertSupportedProducts(order);
        initCustomerOrders(customerId);
        var products = calculateFreeBeverages(customerId, Arrays.asList(order));
        calculateFreeExtras(products);

        return new Receipt(customerId, products);
    }

    private void calculateFreeExtras(List<Product> products) {
        boolean orderedBeverage = products.stream().filter(e -> e.getProductType().equals(ProductType.BEVERAGE)).count() >= 1;
        boolean orderedSnack = products.stream().filter(e -> e.getProductType().equals(ProductType.SNACK)).count() >= 1;

        if (orderedBeverage && orderedSnack) {
            for (int i = 0; i < products.size(); i++) {
                if (products.get(i).getProductType().equals(ProductType.EXTRAS)) {
                    products.set(i, new Product(products.get(i).getName(), BigDecimal.ZERO, products.get(i).getProductType()));
                    break;
                }
            }
        }
    }

    private List<Product> calculateFreeBeverages(Long customerId, List<Product> newOrder) {
        var oldOrders = this.oldOrders.get(customerId);

        for (int i = 0; i < newOrder.size(); i++) {
            if (ProductType.BEVERAGE.equals(newOrder.get(i).getProductType())) {
                oldOrders.add(newOrder.get(i));
                if (oldOrders.size() % LIMIT_FOR_FREE_BEVERAGE == 0) {
                    newOrder.set(i, new Product(newOrder.get(i).getName(), BigDecimal.ZERO, newOrder.get(i).getProductType()));
                    oldOrders = new ArrayList<>();
                }
            }
        }

        return newOrder;
    }

    private void initCustomerOrders(Long customerId) {
        oldOrders.putIfAbsent(customerId, new ArrayList<>());
    }

    private void assertSupportedProducts(Product[] order) {
        Arrays.stream(order).forEach(e -> {
            if (!availableProducts.contains(e)) {
                throw new IllegalArgumentException("Product is not supported");
            }
        });
    }

    private void assertEmptyProductList(Product[] order) {
        if (order.length == 0) {
            throw new IllegalArgumentException("Cannot create a receipt from empty order");
        }
    }

    private List<Product> initProducts() {
        return Arrays.asList(
                new Product("small coffee", "2.50", ProductType.BEVERAGE),
                new Product("medium coffee", "3.00", ProductType.BEVERAGE),
                new Product("large coffee", "3.50", ProductType.BEVERAGE),
                new Product("Bacon Roll", "4.50", ProductType.SNACK),
                new Product("Freshly squeezed orange juice (0.25l)", "3.95", ProductType.BEVERAGE),
                new Product("Extra milk", "0.30", ProductType.EXTRAS),
                new Product("Foamed milk", "0.50", ProductType.EXTRAS),
                new Product("Special roast coffee", "0.90", ProductType.EXTRAS)
        );
    }
}
