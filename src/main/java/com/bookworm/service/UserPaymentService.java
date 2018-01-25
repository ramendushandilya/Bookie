package com.bookworm.service;

import com.bookworm.domain.UserPayment;

/**
 * @author rams0516
 *         Date: 1/23/2018
 *         Time: 4:41 PM
 */
public interface UserPaymentService {

    UserPayment findById(Long id);

    void removeById(Long id);

}