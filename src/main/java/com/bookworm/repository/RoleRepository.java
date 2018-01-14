package com.bookworm.repository;

import com.bookworm.domain.security.Role;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by failedOptimus on 15-01-2018.
 */
public interface RoleRepository extends CrudRepository<Role, Long>{

    Role findByname(String name);

}
