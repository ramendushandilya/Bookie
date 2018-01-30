package com.bookworm.service.impl;

import com.bookworm.domain.*;
import com.bookworm.repository.BookToCartItemRepository;
import com.bookworm.repository.CartItemRepository;
import com.bookworm.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by failedOptimus on 25-01-2018.
 */

@Service
public class CartItemServiceImpl implements CartItemService{

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private BookToCartItemRepository bookToCartItemRepository;

    @Override
    public List<CartItem> findByShoppingCart(ShoppingCart cart) {
        return cartItemRepository.findByShoppingCart(cart);
    }

    @Override
    public CartItem updateCartItem(CartItem cart) {
        BigDecimal bigDecimal = new BigDecimal(cart.getBook().getOurPrice()).multiply(new BigDecimal(cart.getQty()));
        bigDecimal = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
        cart.setSubtotal(bigDecimal);
        cartItemRepository.save(cart);
        return cart;
    }

    @Override
    public CartItem addBookToCartItem(Book book, User user, int qty) {
        List<CartItem> cartItems = findByShoppingCart(user.getShoppingCart());
        for(CartItem items : cartItems) {
            if(book.getId() == items.getBook().getId()) {
                items.setQty(items.getQty()+qty);
                items.setSubtotal(new BigDecimal(book.getOurPrice()).multiply(new BigDecimal(qty)));
                cartItemRepository.save(items);
                return items;
            }
        }

        CartItem cartItem = new CartItem();
        cartItem.setShoppingCart(user.getShoppingCart());
        cartItem.setBook(book);
        cartItem.setQty(qty);
        cartItem.setSubtotal(new BigDecimal(book.getOurPrice()).multiply(new BigDecimal(qty)));
        cartItem = cartItemRepository.save(cartItem);

        BookToCartItem bookToCartItem = new BookToCartItem();
        bookToCartItem.setBook(book);
        bookToCartItem.setCartItem(cartItem);
        bookToCartItemRepository.save(bookToCartItem);
        return cartItem;
    }

    @Override
    public CartItem findById(Long id) {
        return cartItemRepository.findOne(id);
    }

    @Override
    public void removeCartItem(CartItem cartItem) {
        bookToCartItemRepository.deleteByCartItem(cartItem);
        cartItemRepository.delete(cartItem);
    }

    @Override
    public CartItem save(CartItem cart) {
        return cartItemRepository.save(cart);
    }

    @Override
    public List<CartItem> findByOrder(Order order) {
        return cartItemRepository.findByOrder(order);
    }
}
