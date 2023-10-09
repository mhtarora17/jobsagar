package com.spring.assignment.controller;

import com.spring.assignment.dto.BookDataObject;
import com.spring.assignment.exception.ResourceNotFoundException;
import com.spring.assignment.service.IBookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Books controller.
 *
 * @author Mohit Arora
 */
@RestController
@RequestMapping("/api/")
public class BooksRestController {

	@Autowired
	@Qualifier(value = "bookService")
	private IBookService iBookService;


	/**
	 * Get all Bookss list.
	 *
	 * @return the list
	 */
	@GetMapping("/books")
	public List<BookDataObject> getAllBooks() {
		return iBookService.getAllBooks();
	}

	/**
	 * Gets Bookss by id.
	 *
	 * @param BooksId the Books id
	 * @return the Bookss by id
	 * @throws ResourceNotFoundException the resource not found exception
	 */
	@GetMapping("/book/{id}")
	public ResponseEntity<BookDataObject> getBookById(@PathVariable(value = "id") Long bookId)
			throws ResourceNotFoundException {
		BookDataObject bookDataObject = iBookService.getBookById(bookId);
		return ResponseEntity.ok().body(bookDataObject);

	}

	/**
	 * Create Books Books.
	 *
	 * @param Books the Books
	 * @return the Books
	 */
	@PostMapping("/book")
	public BookDataObject createBook(@Valid @RequestBody BookDataObject bookDataObject) {
		return iBookService.createBook(bookDataObject);
	}


	/**
	 * Update Books response entity.
	 *
	 * @param bookId      the Books id
	 * @param bookDetails the Books details
	 * @return the response entity
	 * @throws ResourceNotFoundException the resource not found exception
	 */
	@PutMapping("/book/{id}")
	public ResponseEntity<BookDataObject> updateBooks(@PathVariable(value = "id") Long bookId,
			@Valid @RequestBody BookDataObject bookDataObject) throws ResourceNotFoundException {
		BookDataObject bookDataObject2 = iBookService.updateBooks(bookId, bookDataObject);
		return ResponseEntity.ok(bookDataObject2);
	}

	/**
	 * Delete Books map.
	 *
	 * @param bookId the Books id
	 * @return the map
	 * @throws Exception the exception
	 */
	@DeleteMapping("/book/{id}")
	public Map<String, Boolean> deleteBooks(@PathVariable(value = "id") Long bookId) throws Exception {
		Boolean deleted = false;
		try {
			deleted = iBookService.deleteBooks(bookId);
		}catch (Exception e) {
			deleted = false;
		}
		
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", deleted);
		return response;
	}
}
