package com.bookworm.service;

import com.bookworm.domain.Payment;
import com.bookworm.domain.UserPayment;

/**
 * @author rams0516
 *         Date: 2/3/2018
 *         Time: 2:45 PM
 */
public interface PaymentService {

    Payment setUserPayment(UserPayment userPayment, Payment payment);

}