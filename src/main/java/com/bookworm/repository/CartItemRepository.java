package com.bookworm.repository;

import com.bookworm.domain.CartItem;
import com.bookworm.domain.Order;
import com.bookworm.domain.ShoppingCart;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by failedOptimus on 25-01-2018.
 */

@Transactional
public interface CartItemRepository extends CrudRepository<CartItem, Long> {

    List<CartItem> findByShoppingCart(ShoppingCart cart);

    List<CartItem> findByOrder(Order order);

}
