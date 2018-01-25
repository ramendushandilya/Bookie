package com.bookworm.service.impl;

import com.bookworm.domain.*;
import com.bookworm.repository.OrderRepository;
import com.bookworm.service.CartItemService;
import com.bookworm.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by failedOptimus on 25-01-2018.
 */

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartItemService cartItemService;

    @Override
    public Order createOrder(ShoppingCart shoppingCart,
                             ShippingAddress shippingAddress,
                             BillingAddress billingAddress,
                             Payment payment,
                             String shippingMethod,
                             User user) {

        return null;
    }

    @Override
    public Order findOne(Long id) {
        return orderRepository.findOne(id);
    }
}