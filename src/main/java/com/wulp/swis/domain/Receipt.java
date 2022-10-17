package com.wulp.swis.domain;

import java.math.BigDecimal;
import java.util.List;

public record Receipt(Long customerId, List<Product> productList) {

    public String generateReceipt() {
        var receipt = new StringBuilder("Receipt for customer: " + customerId + "\n");
        for (Product product : productList) {
            receipt.append(String.format("%40s %20.2f CHF\n", product.getName(), product.getPrice()));
        }

        var sumPrice = productList.stream().map(Product::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

        receipt.append("------------------------------------------------------------------------------------------\n");
        receipt.append(String.format("%40s %20.2f CHF\n", "Sum", sumPrice.doubleValue()));

        return receipt.toString();
    }
}
