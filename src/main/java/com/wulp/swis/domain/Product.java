package com.wulp.swis.domain;

import java.math.BigDecimal;

public class Product {
    private final String name;
    private final BigDecimal price;
    private final ProductType productType;

    public Product(String name, String price, ProductType productType) {
        this(name, new BigDecimal(price), productType);
    }

    public Product(String name, BigDecimal price, ProductType productType) {
        this.name = name;
        this.price = price;
        this.productType = productType;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public ProductType getProductType() {
        return productType;
    }

}
