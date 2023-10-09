package com.spring.assignment.service.impl;

import com.spring.assignment.dto.BookDataObject;
import com.spring.assignment.dto.BookInventoryDataObject;
import com.spring.assignment.exception.ResourceNotFoundException;
import com.spring.assignment.model.Book;
import com.spring.assignment.model.BooksInventory;
import com.spring.assignment.repository.BooksInventoryRepository;
import com.spring.assignment.repository.BooksRepository;
import com.spring.assignment.service.IBookService;
import com.spring.assignment.service.IInventoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service(value = "inventoryService")
public class InventoryService implements IInventoryService {

	@Autowired
	private BooksInventoryRepository inventoryRepository;


	@Override
	public Map<Long, Integer> getInventoryData() {
		List<BooksInventory> data = inventoryRepository.findAll();
		System.out.println(data.toString());
		Map<Long, Integer> inventoryData = new HashMap<>();
		for (BooksInventory b : data) {
			inventoryData.put(b.getBookId(), b.getAvailableStock());
		}
		System.out.println(inventoryData);
		return inventoryData;
	}

	public String updateInventory(BookInventoryDataObject bookInventoryDataObject) throws ResourceNotFoundException {
		BooksInventory booksInventory = inventoryRepository.findById(bookInventoryDataObject.getId()).orElseThrow(() -> new ResourceNotFoundException("Books not found on :: " + bookInventoryDataObject.getId()));;
		booksInventory.setAvailableStock(bookInventoryDataObject.getStock());
		inventoryRepository.save(booksInventory);
		return "Data has been updated";
	}
}
