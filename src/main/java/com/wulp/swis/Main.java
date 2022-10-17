package com.wulp.swis;

import com.wulp.swis.service.CoffeeShop;

public class Main {
    public static void main(String[] args) {

        var cs = new CoffeeShop();

        var receipt = cs.createReceipt(
                cs.getAvailableProducts().get(1),
                cs.getAvailableProducts().get(3),
                cs.getAvailableProducts().get(6));

        System.out.println(receipt);

    }
}
