package com.bookworm.repository;

import com.bookworm.domain.BookToCartItem;
import com.bookworm.domain.CartItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author rams0516
 *         Date: 1/29/2018
 *         Time: 5:01 PM
 */

@Transactional
public interface BookToCartItemRepository extends CrudRepository<BookToCartItem, Long>{

    void deleteByCartItem(CartItem item);

}