package com.bookworm.service;

import com.bookworm.domain.Book;

import java.util.List;

/**
 * @author rams0516
 *         Date: 1/22/2018
 *         Time: 3:39 PM
 */
public interface BookService {

    List<Book> findAll();

    Book findOne(Long id);
}
