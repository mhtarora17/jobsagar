package com.spring.assignment.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.spring.assignment.dto.BookDataObject;
import com.spring.assignment.exception.ResourceNotFoundException;

@Component("bookService")
public interface IBookService {
	
	public List<BookDataObject> getAllBooks();
	
	public BookDataObject getBookById(Long bookId) throws ResourceNotFoundException;
	
	public BookDataObject createBook(BookDataObject book);
	
	public BookDataObject updateBooks(Long bookId,BookDataObject bookDataObject) throws ResourceNotFoundException;
	
	public Boolean deleteBooks(Long bookId) throws Exception;
}
