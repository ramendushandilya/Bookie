package com.bookworm.service.impl;

import com.bookworm.domain.CartItem;
import com.bookworm.domain.ShoppingCart;
import com.bookworm.repository.ShoppingCartRepository;
import com.bookworm.service.CartItemService;
import com.bookworm.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author rams0516
 *         Date: 1/29/2018
 *         Time: 4:58 PM
 */

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService{

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Override
    public ShoppingCart updateShoppingCart(ShoppingCart cart) {
        BigDecimal cartTotal = new BigDecimal(0);
        List<CartItem> cartItems = cartItemService.findByShoppingCart(cart);

        for(CartItem item : cartItems) {
            if(item.getBook().getInStockNumber() > 0) {
                cartItemService.updateCartItem(item);
                cartTotal = cartTotal.add(item.getSubtotal());
            }
        }
        cart.setGrandTotal(cartTotal);
        shoppingCartRepository.save(cart);
        return cart;
    }

    @Override
    public void clearShoppingCart(ShoppingCart cart) {
        List<CartItem> cartItems = cartItemService.findByShoppingCart(cart);

        for(CartItem item : cartItems) {
            item.setShoppingCart(null);
            cartItemService.save(item);
        }

        cart.setGrandTotal(new BigDecimal(0));
        shoppingCartRepository.save(cart);
    }
}