package com.example.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.dto.book.BookDto;
import com.example.dto.book.CreateBookRequestDto;
import com.example.exception.EntityNotFoundException;
import com.example.util.TestConstants;
import com.example.util.TestUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
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
    private final TestUtil testUtil = new TestUtil();
    private CreateBookRequestDto requestDto;
    private CreateBookRequestDto updateBookRequestDto;
    private BookDto bookDtoAfterUpdate;
    private BookDto bookDtoOne;
    private BookDto bookDtoTwo;
    private BookDto bookDtoThree;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @BeforeEach
    void setUp() {
        requestDto = testUtil.getCreateBookRequestDto();
        bookDtoOne = testUtil.getBookDtoOne();
        bookDtoTwo = testUtil.getBookDtoTwo();
        bookDtoThree = testUtil.getBookDtoThree();
        updateBookRequestDto = testUtil.getUpdateBookRequestDto();
        bookDtoAfterUpdate = testUtil.getBookDtoAfterUpdate();
    }

    @WithMockUser
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
        MvcResult result = mockMvc.perform(get(TestConstants.URL_BOOK)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String responseContent = result.getResponse().getContentAsString();
        JsonNode root = objectMapper.readTree(responseContent);
        JsonNode content = root.get(TestConstants.GET_CONTENT);
        List<BookDto> actual = objectMapper.readerForListOf(BookDto.class).readValue(content);
        assertNotNull(actual);
        assertEquals(TestConstants.THREE, actual.size());
        assertTrue(EqualsBuilder.reflectionEquals(bookDtoOne,
                actual.get(TestConstants.ZERO), TestConstants.EXCLUDE_ID));
        assertTrue(EqualsBuilder.reflectionEquals(bookDtoTwo,
                actual.get(TestConstants.ONE.intValue()), TestConstants.EXCLUDE_ID));
        assertTrue(EqualsBuilder.reflectionEquals(bookDtoThree,
                actual.get(TestConstants.TWO.intValue()), TestConstants.EXCLUDE_ID));
    }

    @Test
    @WithMockUser
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
        MvcResult result = mockMvc.perform(get(TestConstants.URL_BOOK_ID, TestConstants.ONE)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        BookDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), BookDto.class);
        assertTrue(EqualsBuilder.reflectionEquals(bookDtoOne, actual,
                TestConstants.EXCLUDE_ID));
    }

    @Test
    @WithMockUser
    @DisplayName("Get book bi invalid Id should throw EntityNotFoundException")
    void getBookByIdValidId_ShouldThrowEntityNotFoundException() throws Exception {
        mockMvc.perform(get(TestConstants.URL_BOOK_ID, TestConstants.NON_EXISTING_ID)
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
                post(TestConstants.URL_BOOK)
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated()).andReturn();
        BookDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), BookDto.class);
        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertTrue(EqualsBuilder.reflectionEquals(bookDtoOne, actual, TestConstants.EXCLUDE_ID));
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
        mockMvc.perform(get(TestConstants.URL_BOOK_ID, TestConstants.ONE))
                        .andExpect(status().isOk());
        mockMvc.perform(delete(TestConstants.URL_BOOK_ID, TestConstants.ONE))
                .andExpect(status().isNoContent());
        mockMvc.perform(get(TestConstants.URL_BOOK_ID, TestConstants.ONE))
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
        mockMvc.perform(get(TestConstants.URL_BOOK_ID, TestConstants.ONE))
                .andExpect(status().isOk());
        String jsonRequest = objectMapper.writeValueAsString(updateBookRequestDto);
        MvcResult result = mockMvc.perform(put(TestConstants.URL_BOOK_ID, TestConstants.ONE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest)
                ).andExpect(status().isOk())
                .andReturn();
        BookDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), BookDto.class);
        assertNotNull(actual);
        assertTrue(EqualsBuilder.reflectionEquals(bookDtoAfterUpdate,
                actual, TestConstants.EXCLUDE_ID));
    }

    @WithMockUser
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
        String searchParameter = "?title=" + TestConstants.BOOK_TITLE_ONE
                + "&author=" + TestConstants.BOOK_AUTHOR_ONE
                + "&isbn=" + TestConstants.BOOK_ISBN_ONE;
        MvcResult result = mockMvc.perform(get(TestConstants.URL_BOOK_SEARCH + searchParameter)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String responseContent = result.getResponse().getContentAsString();
        JsonNode root = objectMapper.readTree(responseContent);
        JsonNode content = root.get(TestConstants.GET_CONTENT);
        List<BookDto> bookDtoList = objectMapper.readerForListOf(BookDto.class).readValue(content);
        assertNotNull(bookDtoList);
        assertTrue(EqualsBuilder.reflectionEquals(bookDtoOne,
                bookDtoList.get(TestConstants.ZERO), TestConstants.EXCLUDE_ID));
    }
}
