package com.bookworm.repository;

import com.bookworm.domain.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by failedOptimus on 14-01-2018.
 */
public interface UserRepository extends CrudRepository<User, Long>{

    User findByUsername(String username);

    User findByEmail(String email);

}