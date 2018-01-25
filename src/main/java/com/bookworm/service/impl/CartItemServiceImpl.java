package com.bookworm.service.impl;

import com.bookworm.domain.*;
import com.bookworm.repository.CartItemRepository;
import com.bookworm.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by failedOptimus on 25-01-2018.
 */

@Service
public class CartItemServiceImpl implements CartItemService{

    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public List<CartItem> findByShoppingCart(ShoppingCart cart) {
        return null;
    }

    @Override
    public CartItem updateCartItem(CartItem cart) {
        return null;
    }

    @Override
    public CartItem addBookToCartItem(Book book, User user, int quantity) {
        return null;
    }

    @Override
    public CartItem findById(Long id) {
        return null;
    }

    @Override
    public void removeCartItem(CartItem cartItem) {

    }

    @Override
    public CartItem save(CartItem cart) {
        return null;
    }

    @Override
    public List<CartItem> findByOrder(Order order) {
        return null;
    }
}
