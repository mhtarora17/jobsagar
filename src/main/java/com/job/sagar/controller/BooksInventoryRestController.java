package com.job.sagar.controller;

import com.job.sagar.service.IInventoryService;
import com.job.sagar.dto.BookInventoryDataObject;
import com.job.sagar.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The type Books controller.
 *
 * @author Mohit Arora
 */
@RestController
@RequestMapping("/api/")
public class BooksInventoryRestController {

	@Autowired
	@Qualifier(value = "inventoryService")
	private IInventoryService iInventoryService;

	@GetMapping("/booksInventory")
	public Map<Long, Integer> getInventoryData() {
		return iInventoryService.getInventoryData();
	}

	@PutMapping("/updateBookInventory")
	public String updateBookInventory(@Valid @RequestBody BookInventoryDataObject bookDataObject) throws ResourceNotFoundException {
		return iInventoryService.updateInventory(bookDataObject);

	}




}
