package com.example.repository.book;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.model.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {
    private static final int ZERO = 0;
    private static final int TEN = 10;
    private static final Long ONE = 1L;

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Find all books by categories ID")
    @Sql(scripts = {
            "classpath:database/book/add-book-to-books-table.sql",
            "classpath:database/category/add-category-to-categories-table.sql",
            "classpath:database/book/add-category-to-the-book-in-books_categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/book/remove-book-id-category-id-from-books_categories-table.sql",
            "classpath:database/category/remove-category-from-categories-table.sql",
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllByCategoryId() {
        Page<Book> result = bookRepository.findAllByCategoryId(ONE, PageRequest.of(ZERO, TEN));

        assertNotNull(result);
        assertEquals(ONE.intValue(), result.getTotalElements());
    }
}
