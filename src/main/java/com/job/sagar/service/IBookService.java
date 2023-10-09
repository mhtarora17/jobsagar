package com.job.sagar.service;

import java.util.List;

import com.job.sagar.dto.BookDataObject;
import com.job.sagar.exception.ResourceNotFoundException;
import org.springframework.stereotype.Component;

@Component("bookService")
public interface IBookService {
	
	public List<BookDataObject> getAllBooks();
	
	public BookDataObject getBookById(Long bookId) throws ResourceNotFoundException;
	
	public BookDataObject createBook(BookDataObject book);
	
	public BookDataObject updateBooks(Long bookId,BookDataObject bookDataObject) throws ResourceNotFoundException;
	
	public Boolean deleteBooks(Long bookId) throws Exception;
}
