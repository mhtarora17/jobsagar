package com.staxrt.tutorial;

import com.job.sagar.JobSagarApplication;
import com.job.sagar.model.Book;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = JobSagarApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JobSagarApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	private String getRootUrl() {
		return "http://localhost:";
	}

	@Test
	public void contextLoads() {
	}

	@Test
	public void testGetAllBooks() {
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/books", HttpMethod.GET, entity,
				String.class);

		Assert.assertNotNull(response.getBody());
	}

	@Test
	public void testGetUserById() {
		Book book = restTemplate.getForObject(getRootUrl() + "/books/1", Book.class);
		System.out.println(book.getBookName());
		Assert.assertNotNull(book);
	}

	@Test
	public void testCreateBook() {
		Book book = new Book();
		book.setAuthorName("Mohit Arora");
		book.setBookName("Spring Assignment");
		book.setCreatedBy("mohit");
		book.setUpdatedBy("mohit");

		ResponseEntity<Book> postResponse = restTemplate.postForEntity(getRootUrl() + "/books", book, Book.class);
		Assert.assertNotNull(postResponse);
		Assert.assertNotNull(postResponse.getBody());
	}

	@Test
	public void testUpdatePost() {
		int id = 1;
		Book book = restTemplate.getForObject(getRootUrl() + "/books/" + id, Book.class);
		book.setAuthorName("Mohit Arora 1");
		book.setBookName("Spring Assignment 1");

		restTemplate.put(getRootUrl() + "/books/" + id, book);

		Book updatedBook = restTemplate.getForObject(getRootUrl() + "/books/" + id, Book.class);
		Assert.assertNotNull(updatedBook);
	}

	@Test
	public void testDeletePost() {
		int id = 2;
		Book book = restTemplate.getForObject(getRootUrl() + "/books/" + id, Book.class);
		Assert.assertNotNull(book);

		restTemplate.delete(getRootUrl() + "/books/" + id);

		try {
			book = restTemplate.getForObject(getRootUrl() + "/books/" + id, Book.class);
		} catch (final HttpClientErrorException e) {
			Assert.assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
		}
	}

}
