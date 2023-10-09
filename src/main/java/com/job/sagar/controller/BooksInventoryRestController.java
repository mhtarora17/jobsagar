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

	static public String naming = "mohit"; // the value will be null

	private int num; // the value will be 0

	@GetMapping("/booksInventory")
	public Map<Long, Integer> getInventoryData() {

		String name = "mohit";
		System.out.println(name);
		return iInventoryService.getInventoryData();
	}

	@GetMapping("/startMonitoring")
	public String startMonitoring() {
		ExecutorService ex = Executors.newFixedThreadPool(2);
		ex.execute(() -> {
			while (true) {
				try {
					Thread.sleep(1000l);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
				System.out.println(getInventoryData());
			}
		} );
		return "Monitoring has been started";
	}

	@PutMapping("/updateBookInventory")
	public String updateBookInventory(@Valid @RequestBody BookInventoryDataObject bookDataObject) throws ResourceNotFoundException {
		return iInventoryService.updateInventory(bookDataObject);

	}




}
