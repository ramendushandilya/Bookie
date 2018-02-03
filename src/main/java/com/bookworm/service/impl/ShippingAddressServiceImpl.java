package com.bookworm.service.impl;

import com.bookworm.domain.ShippingAddress;
import com.bookworm.domain.UserShipping;
import com.bookworm.service.ShippingAddressService;
import org.springframework.stereotype.Service;

/**
 * @author rams0516
 *         Date: 2/3/2018
 *         Time: 2:29 PM
 */

@Service
public class ShippingAddressServiceImpl implements ShippingAddressService {

    @Override
    public ShippingAddress setByUserShipping(UserShipping userShipping, ShippingAddress shippingAddress) {
        shippingAddress.setId(userShipping.getId());
        shippingAddress.setShippingAddressCity(userShipping.getUserShippingCity());
        shippingAddress.setShippingAddressCountry(userShipping.getUserShippingCountry());
        shippingAddress.setShippingAddressName(userShipping.getUserShippingName());
        shippingAddress.setShippingAddressState(userShipping.getUserShippingState());
        shippingAddress.setShippingAddressStreet1(userShipping.getUserShippingStreet1());
        shippingAddress.setShippingAddressStreet2(userShipping.getUserShippingStreet2());
        shippingAddress.setShippingAddressZipcode(userShipping.getUserShippingZipcode());
        return shippingAddress;
    }
}