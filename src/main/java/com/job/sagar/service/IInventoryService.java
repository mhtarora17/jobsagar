package com.job.sagar.service;

import com.job.sagar.dto.BookInventoryDataObject;
import com.job.sagar.exception.ResourceNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("inventoryService")
public interface IInventoryService {
	
	public Map<Long, Integer> getInventoryData();

	public String updateInventory(BookInventoryDataObject bookInventoryDataObject) throws ResourceNotFoundException;
}
