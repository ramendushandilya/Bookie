package com.bookworm.service;

import com.bookworm.domain.*;

/**
 * Created by failedOptimus on 25-01-2018.
 */
public interface OrderService {

    Order createOrder(ShoppingCart shoppingCart,
                      ShippingAddress shippingAddress,
                      BillingAddress billingAddress,
                      Payment payment,
                      String shippingMethod,
                      User user);

    Order findOne(Long id);

}
