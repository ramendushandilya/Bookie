package com.bookworm.service.impl;

import com.bookworm.domain.Book;
import com.bookworm.repository.BookRepository;
import com.bookworm.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author rams0516
 *         Date: 1/22/2018
 *         Time: 3:53 PM
 */

@Service
public class BookServiceImpl implements BookService{

    @Autowired
    private BookRepository bookRepository;

    @Override
    public List<Book> findAll() {
        return (List<Book>) bookRepository.findAll();
    }

    @Override
    public Book findOne(Long id) {
        return bookRepository.findOne(id);
    }
}