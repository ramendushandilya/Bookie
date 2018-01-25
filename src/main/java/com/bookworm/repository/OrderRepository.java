package com.bookworm.repository;

import com.bookworm.domain.Order;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by failedOptimus on 25-01-2018.
 */
public interface OrderRepository extends CrudRepository<Order, Long>{



}
