package com.spring.assignment.service;

import com.spring.assignment.dto.BookDataObject;
import com.spring.assignment.dto.BookInventoryDataObject;
import com.spring.assignment.exception.ResourceNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component("inventoryService")
public interface IInventoryService {
	
	public Map<Long, Integer> getInventoryData();

	public String updateInventory(BookInventoryDataObject bookInventoryDataObject) throws ResourceNotFoundException;
}
