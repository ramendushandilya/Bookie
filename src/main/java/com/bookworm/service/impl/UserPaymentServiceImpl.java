package com.bookworm.service.impl;

import com.bookworm.domain.UserPayment;
import com.bookworm.repository.UserPaymentRepository;
import com.bookworm.service.UserPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author rams0516
 *         Date: 1/23/2018
 *         Time: 4:42 PM
 */

@Service
public class UserPaymentServiceImpl implements UserPaymentService{

    @Autowired
    private UserPaymentRepository userPaymentRepository;

    @Override
    public UserPayment findById(Long id) {
        return userPaymentRepository.findOne(id);
    }

}