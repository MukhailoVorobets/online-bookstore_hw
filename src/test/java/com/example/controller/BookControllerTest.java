package com.example.controller;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.dto.book.BookDto;
import com.example.dto.book.CreateBookRequestDto;
import com.example.exception.EntityNotFoundException;
import com.example.model.Category;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {
    protected static MockMvc mockMvc;
    private static final Long ONE = 1L;
    private static final Long TWO = 2L;
    private static final Long THREE = 3L;
    private static final String CATEGORY_NAME = "Category";
    private static final String CATEGORY_DESCRIPTION = "Description";
    private static final String BOOK_TITLE_1 = "Title test1";
    private static final String BOOK_AUTHOR_1 = "Author test1";
    private static final String BOOK_ISBN_1 = "ISBN1234567891";
    private static final BigDecimal BOOK_PRICE_1 = BigDecimal.valueOf(9.99);
    private static final String BOOK_DESCRIPTION_1 = "Description test1";
    private static final String BOOK_COVER_IMAGE_1 = "https://example.com/tast-cover-image-1.jpg";
    private static final String BOOK_TITLE_2 = "Title test2";
    private static final String BOOK_AUTHOR_2 = "Author test2";
    private static final String BOOK_ISBN_2 = "ISBN1234567892";
    private static final BigDecimal BOOK_PRICE_2 = BigDecimal.valueOf(99.99);
    private static final String BOOK_DESCRIPTION_2 = "Description test2";
    private static final String BOOK_COVER_IMAGE_2 = "https://example.com/tast-cover-image-2.jpg";
    private static final String BOOK_TITLE_3 = "Title test3";
    private static final String BOOK_AUTHOR_3 = "Author test3";
    private static final String BOOK_ISBN_3 = "ISBN1234567893";
    private static final BigDecimal BOOK_PRICE_3 = BigDecimal.valueOf(999.99);
    private static final String BOOK_DESCRIPTION_3 = "Description test3";
    private static final String BOOK_COVER_IMAGE_3 = "https://example.com/tast-cover-image.jpg-3";
    private static final String UPDATE_BOOK_TITLE = "Update Title";
    private static final String UPDATE_BOOK_AUTHOR = "Update Author";
    private static final String UPDATE_BOOK_ISBN = "ISBN1234567899";
    private static final BigDecimal UPDATE_BOOK_PRICE = BigDecimal.valueOf(99.9);
    private static final String UPDATE_BOOK_DESCRIPTION = "Update Description test";
    private static final String UPDATE_BOOK_COVER_IMAGE = "https://example.com/tast-cover-image-update.jpg";
    private CreateBookRequestDto requestDto;
    private CreateBookRequestDto updateBookRequestDto;
    private BookDto bookDto1;
    private BookDto bookDto2;
    private BookDto bookDto3;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext
    ) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @BeforeEach
    void setUp() {
        Category category = new Category();
        category.setName(CATEGORY_NAME);
        category.setDescription(CATEGORY_DESCRIPTION);

        requestDto = new CreateBookRequestDto();
        requestDto.setTitle(BOOK_TITLE_1);
        requestDto.setAuthor(BOOK_AUTHOR_1);
        requestDto.setIsbn(BOOK_ISBN_1);
        requestDto.setPrice(BOOK_PRICE_1);
        requestDto.setDescription(BOOK_DESCRIPTION_1);
        requestDto.setCoverImage(BOOK_COVER_IMAGE_1);
        requestDto.setCategoryIds(Set.of(ONE));

        updateBookRequestDto = new CreateBookRequestDto();
        updateBookRequestDto.setTitle(UPDATE_BOOK_TITLE);
        updateBookRequestDto.setAuthor(UPDATE_BOOK_AUTHOR);
        updateBookRequestDto.setIsbn(UPDATE_BOOK_ISBN);
        updateBookRequestDto.setPrice(UPDATE_BOOK_PRICE);
        updateBookRequestDto.setDescription(UPDATE_BOOK_DESCRIPTION);
        updateBookRequestDto.setCoverImage(UPDATE_BOOK_COVER_IMAGE);
        updateBookRequestDto.setCategoryIds(Set.of(TWO));

        bookDto1 = new BookDto();
        bookDto1.setId(ONE);
        bookDto1.setTitle(BOOK_TITLE_1);
        bookDto1.setAuthor(BOOK_AUTHOR_1);
        bookDto1.setIsbn(BOOK_ISBN_1);
        bookDto1.setPrice(BOOK_PRICE_1);
        bookDto1.setDescription(BOOK_DESCRIPTION_1);
        bookDto1.setCoverImage(BOOK_COVER_IMAGE_1);
        bookDto1.setCategoryIds(Set.of(ONE));

        bookDto2 = new BookDto();
        bookDto2.setId(TWO);
        bookDto2.setTitle(BOOK_TITLE_2);
        bookDto2.setAuthor(BOOK_AUTHOR_2);
        bookDto2.setIsbn(BOOK_ISBN_2);
        bookDto2.setPrice(BOOK_PRICE_2);
        bookDto2.setDescription(BOOK_DESCRIPTION_2);
        bookDto2.setCoverImage(BOOK_COVER_IMAGE_2);
        bookDto2.setCategoryIds(Set.of(TWO));

        bookDto3 = new BookDto();
        bookDto3.setId(THREE);
        bookDto3.setTitle(BOOK_TITLE_3);
        bookDto3.setAuthor(BOOK_AUTHOR_3);
        bookDto3.setIsbn(BOOK_ISBN_3);
        bookDto3.setPrice(BOOK_PRICE_3);
        bookDto3.setDescription(BOOK_DESCRIPTION_3);
        bookDto3.setCoverImage(BOOK_COVER_IMAGE_3);
        bookDto3.setCategoryIds(Set.of(THREE));
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("Get all books")
    @Sql(scripts = {
            "classpath:database/book/remove-book-id-category-id-from-books_categories-table.sql",
            "classpath:database/book/remove-book-from-books-table.sql",
            "classpath:database/category/remove-category-from-categories-table.sql",
            "classpath:database/book/add-book-to-books-table.sql",
            "classpath:database/category/add-category-to-categories-table.sql",
            "classpath:database/book/add-category-to-the-book-in-books_categories-table.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/book/remove-book-id-category-id-from-books_categories-table.sql",
            "classpath:database/book/remove-book-from-books-table.sql",
            "classpath:database/category/remove-category-from-categories-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAll_GivenBookInCatalog_ShouldReturnAllBook() throws Exception {
        MvcResult result = mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        JsonNode root = objectMapper.readTree(responseContent);
        JsonNode content = root.get("content");

        List<BookDto> actual = objectMapper.readerForListOf(BookDto.class).readValue(content);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(3, actual.size());
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(bookDto1, actual.get(0), "id"));
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(bookDto2, actual.get(1), "id"));
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(bookDto3, actual.get(2), "id"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Get book bi valid id")
    @Sql(scripts = {
            "classpath:database/book/add-book-to-books-table.sql",
            "classpath:database/category/add-category-to-categories-table.sql",
            "classpath:database/book/add-category-to-the-book-in-books_categories-table.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/book/remove-book-id-category-id-from-books_categories-table.sql",
            "classpath:database/book/remove-book-from-books-table.sql",
            "classpath:database/category/remove-category-from-categories-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getBookByIdValidId_ShouldReturnBookDto() throws Exception {
        MvcResult result = mockMvc.perform(get("/books/{id}", ONE)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        BookDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), BookDto.class);
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(bookDto1, actual, "id"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Get book bi invalid Id should throw EntityNotFoundException")
    void getBookByIdValidId_ShouldThrowEntityNotFoundException() throws Exception {
        mockMvc.perform(get("/books/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertInstanceOf(EntityNotFoundException.class,
                        result.getResolvedException()))
                .andReturn();
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Create a new book")
    @Sql(scripts =
            "classpath:database/category/add-category-to-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/book/remove-book-id-category-id-from-books_categories-table.sql",
            "classpath:database/book/remove-book-from-books-table.sql",
            "classpath:database/category/remove-category-from-categories-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createBook_ValidRequestDto_Success() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(
                post("/books")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated()).andReturn();

        BookDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), BookDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(bookDto1, actual, "id"));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Delete book")
    @Sql(scripts =
            "classpath:database/book/add-book-to-books-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/book/remove-book-id-category-id-from-books_categories-table.sql",
            "classpath:database/book/remove-book-from-books-table.sql",
            "classpath:database/category/remove-category-from-categories-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void delete_ValidId_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(get("/books/{id}", ONE))
                        .andExpect(status().isOk());
        mockMvc.perform(delete("/books/{id}", ONE))
                .andExpect(status().isNoContent());
        mockMvc.perform(get("/books/{id}", ONE))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Update book")
    @Sql(scripts = {
            "classpath:database/book/add-book-to-books-table.sql",
            "classpath:database/category/add-category-to-categories-table.sql",
            "classpath:database/book/add-category-to-the-book-in-books_categories-table.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/book/remove-book-id-category-id-from-books_categories-table.sql",
            "classpath:database/book/remove-book-from-books-table.sql",
            "classpath:database/category/remove-category-from-categories-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void update_ValidInput_ShouldUpdateAndReturnBook() throws Exception {
        mockMvc.perform(get("/books/{id}", ONE))
                .andExpect(status().isOk());

        String jsonRequest = objectMapper.writeValueAsString(updateBookRequestDto);

        MvcResult result = mockMvc.perform(put("/books/{id}", ONE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest)
                ).andExpect(status().isOk())
                .andReturn();
        BookDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), BookDto.class);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(updateBookRequestDto.getTitle(), actual.getTitle());
        Assertions.assertEquals(updateBookRequestDto.getAuthor(), actual.getAuthor());
        Assertions.assertEquals(updateBookRequestDto.getPrice(), actual.getPrice());
        Assertions.assertEquals(updateBookRequestDto.getDescription(), actual.getDescription());
        Assertions.assertEquals(updateBookRequestDto.getCoverImage(), actual.getCoverImage());
        Assertions.assertEquals(updateBookRequestDto.getCategoryIds(), actual.getCategoryIds());
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("Search book")
    @Sql(scripts = {
            "classpath:database/book/add-book-to-books-table.sql",
            "classpath:database/category/add-category-to-categories-table.sql",
            "classpath:database/book/add-category-to-the-book-in-books_categories-table.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/book/remove-book-id-category-id-from-books_categories-table.sql",
            "classpath:database/book/remove-book-from-books-table.sql",
            "classpath:database/category/remove-category-from-categories-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void search_ValidId_ShouldReturnBook() throws Exception {
        String searchParameter = "?title=" + BOOK_TITLE_1
                + "&author=" + BOOK_AUTHOR_1 + "&isbn=" + BOOK_ISBN_1;

        MvcResult result = mockMvc.perform(get("/books/search" + searchParameter)
                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn();
        BookDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), BookDto.class);

        Assertions.assertNotNull(actual);
        EqualsBuilder.reflectionEquals(bookDto1, actual, "id");
    }
}
