package com.bookworm.service.impl;

import com.bookworm.domain.Payment;
import com.bookworm.domain.UserPayment;
import com.bookworm.service.PaymentService;
import org.springframework.stereotype.Service;

/**
 * @author rams0516
 *         Date: 2/3/2018
 *         Time: 2:46 PM
 */

@Service
public class PaymentServiceImpl implements PaymentService {

    @Override
    public Payment setUserPayment(UserPayment userPayment, Payment payment) {
        payment.setType(userPayment.getType());
        payment.setHolderName(userPayment.getHolderName());
        payment.setCardNumber(userPayment.getCardNumber());
        payment.setExpiryMonth(userPayment.getExpiryMonth());
        payment.setExpiryYear(userPayment.getExpiryYear());
        payment.setCvc(userPayment.getCvc());
        return payment;
    }
}