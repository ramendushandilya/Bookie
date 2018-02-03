package com.bookworm.service;

import com.bookworm.domain.ShippingAddress;
import com.bookworm.domain.UserShipping;

/**
 * @author rams0516
 *         Date: 2/3/2018
 *         Time: 2:28 PM
 */
public interface ShippingAddressService {

    ShippingAddress setByUserShipping(UserShipping userShipping, ShippingAddress shippingAddress);

}