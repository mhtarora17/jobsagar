package com.spring.assignment.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.spring.assignment.model.BooksInventory;
import com.spring.assignment.repository.BooksInventoryRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.assignment.dto.BookDataObject;
import com.spring.assignment.exception.ResourceNotFoundException;
import com.spring.assignment.model.Book;
import com.spring.assignment.repository.BooksRepository;
import com.spring.assignment.service.IBookService;

@Service(value = "bookService")
public class BookService implements IBookService {

	@Autowired
	private BooksRepository booksRepository;

	@Autowired
	private BooksInventoryRepository inventoryRepository;

	@Override
	public List<BookDataObject> getAllBooks() {
		List<Book> books  = booksRepository.findAll();
		List<BookDataObject> bookDataObjects = new ArrayList<BookDataObject>();
		for(Book book : books) {
			BookDataObject bookDataObject = new BookDataObject();
			BeanUtils.copyProperties(book, bookDataObject);
			bookDataObjects.add(bookDataObject);
		}
		return bookDataObjects;	
	}

	@Override
	public BookDataObject getBookById(Long bookId) throws ResourceNotFoundException {
		Book book =
	    		booksRepository
	            .findById(bookId)
	            .orElseThrow(() -> new ResourceNotFoundException("Book not found for :: " + bookId));
		
		BookDataObject bookDataObject = new BookDataObject();
		BeanUtils.copyProperties(book, bookDataObject);
		return bookDataObject;
	}

	@Override
	public BookDataObject createBook(BookDataObject bookDataObject) {
		Book book = new Book();
		BeanUtils.copyProperties(bookDataObject, book);
		Book book1 = booksRepository.save(book);
		BooksInventory booksInventory = new BooksInventory();
		booksInventory.setBookId(book1.getId());
		booksInventory.setAvailableStock(0);
		booksInventory.setPrice(100);
		inventoryRepository.save(booksInventory);
		return bookDataObject;
	}

	@Override
	public BookDataObject updateBooks(Long bookId, BookDataObject bookDataObject) throws ResourceNotFoundException {
		Book Books =
	    		booksRepository
	            .findById(bookId)
	            .orElseThrow(() -> new ResourceNotFoundException("Books not found on :: " + bookId));

	    Books.setAuthorName(bookDataObject.getAuthorName());
	    Books.setUpdatedAt(new Date());
	    final Book updatedBook = booksRepository.save(Books);
	    BookDataObject bookDataObject2 = new BookDataObject();
	    BeanUtils.copyProperties(updatedBook, bookDataObject2);
	    
		return bookDataObject2;
	}

	@Override
	public Boolean deleteBooks(Long bookId) throws Exception {
		Book Books = booksRepository.findById(bookId)
				.orElseThrow(() -> new ResourceNotFoundException("Books not found on :: " + bookId));
		booksRepository.delete(Books);
		return true;
	}
	
}
