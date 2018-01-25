package com.bookworm.service;

import com.bookworm.domain.*;

import java.util.List;

/**
 * Created by failedOptimus on 25-01-2018.
 */
public interface CartItemService {

    List<CartItem> findByShoppingCart(ShoppingCart cart);

    CartItem updateCartItem(CartItem cart);

    CartItem addBookToCartItem(Book book, User user, int quantity);

    CartItem findById(Long id);

    void removeCartItem(CartItem cartItem);

    CartItem save(CartItem cart);

    List<CartItem> findByOrder(Order order);

}
