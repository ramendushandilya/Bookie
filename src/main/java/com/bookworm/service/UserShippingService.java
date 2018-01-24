package com.bookworm.service;

import com.bookworm.domain.UserShipping;

/**
 * Created by failedOptimus on 24-01-2018.
 */
public interface UserShippingService {

    UserShipping findById(Long id);

    void removeById(Long id);
}