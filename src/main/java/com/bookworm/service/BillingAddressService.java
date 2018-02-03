package com.bookworm.service;

import com.bookworm.domain.BillingAddress;
import com.bookworm.domain.UserBilling;

/**
 * @author rams0516
 *         Date: 2/3/2018
 *         Time: 2:50 PM
 */
public interface BillingAddressService {

    BillingAddress setByUserBilling(UserBilling userBilling, BillingAddress billingAddress);

}