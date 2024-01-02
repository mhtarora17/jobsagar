package com.job.sagar.model;

import jakarta.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "books_inventory")
@EntityListeners(AuditingEntityListener.class)
public class BooksInventory {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "book_id", nullable = false)
	private Long bookId;

	@Column(name = "available_stock", nullable = false)
	private Integer availableStock;

	@Column(name = "price", nullable = false)
	private Integer price;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Long getBookId() {
		return bookId;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}

	public Integer getAvailableStock() {
		return availableStock;
	}

	public void setAvailableStock(Integer availableStock) {
		this.availableStock = availableStock;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "BooksInventory{" +
				"bookId=" + bookId +
				", availableStock=" + availableStock +
				'}';
	}
}
