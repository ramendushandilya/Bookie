package com.bookworm.domain.security;

import org.springframework.security.core.GrantedAuthority;

/**
 * Created by failedOptimus on 14-01-2018.
 */
public class Authority implements GrantedAuthority{

    private final String authority;

    public Authority(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}
