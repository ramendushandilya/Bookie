package com.bookworm.service;

import com.bookworm.domain.ShoppingCart;

/**
 * @author rams0516
 *         Date: 1/29/2018
 *         Time: 4:56 PM
 */
public interface ShoppingCartService {

    ShoppingCart updateShoppingCart(ShoppingCart cart);

    void clearShoppingCart(ShoppingCart cart);

}