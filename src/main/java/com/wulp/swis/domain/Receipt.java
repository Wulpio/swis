package com.wulp.swis.domain;

import java.math.BigDecimal;
import java.util.List;

public record Receipt(Long customerId, List<Product> productList) {

    public String generateReceipt() {
        var receipt = new StringBuilder("Receipt for customer: " + customerId + "\n");
        productList.forEach(e -> receipt.append(String.format("%40s %20.2f CHF\n", e.getName(), e.getPrice())));

        receipt.append("------------------------------------------------------------------------------------------\n");

        var sumPrice = productList.stream().map(Product::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        receipt.append(String.format("%40s %20.2f CHF\n", "Sum", sumPrice.doubleValue()));

        return receipt.toString();
    }
}
