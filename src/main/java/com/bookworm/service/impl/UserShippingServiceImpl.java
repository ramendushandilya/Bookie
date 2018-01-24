package com.bookworm.service.impl;

import com.bookworm.domain.UserShipping;
import com.bookworm.repository.UserShippingRespository;
import com.bookworm.service.UserShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by failedOptimus on 24-01-2018.
 */

@Service
public class UserShippingServiceImpl implements UserShippingService{

    @Autowired
    private UserShippingRespository userShippingRespository;

    @Override
    public UserShipping findById(Long id) {
        return userShippingRespository.findOne(id);
    }

    @Override
    public void removeById(Long id) {
        userShippingRespository.delete(id);
    }
}
