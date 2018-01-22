package com.bookworm.repository;

import com.bookworm.domain.Book;
import org.springframework.data.repository.CrudRepository;

/**
 * @author rams0516
 *         Date: 1/22/2018
 *         Time: 3:55 PM
 */
public interface BookRepository extends CrudRepository<Book, Long>{



}