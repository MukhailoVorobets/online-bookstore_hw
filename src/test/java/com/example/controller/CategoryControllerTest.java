package com.example.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.dto.book.BookDto;
import com.example.dto.category.CategoryDto;
import com.example.dto.category.CreateCategoryRequestDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
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
class CategoryControllerTest {
    protected static MockMvc mockMvc;
    private static final String URL = "/categories";
    private static final String URL_ID = "/categories/{id}";
    private static final String EMPTY_STRING = "";
    private static final Long ONE = 1L;
    private static final Long TWO = 2L;
    private static final Long THREE = 3L;
    private static final Long NON_EXISTENT_ID = 999L;
    private static final String CATEGORY_NAME = "Category";
    private static final String CATEGORY_DESCRIPTION = "Description";
    private static final String CATEGORY_NAME_1 = "Test name1";
    private static final String CATEGORY_DESCRIPTION_1 = "Description test1";
    private static final String CATEGORY_NAME_2 = "Test name2";
    private static final String CATEGORY_DESCRIPTION_2 = "Description test2";
    private static final String CATEGORY_NAME_3 = "Test name3";
    private static final String CATEGORY_DESCRIPTION_3 = "Description test3";
    private static final String UPDATE_CATEGORY_NAME = "Update Category name";
    private static final String UPDATE__CATEGORY_DESCRIPTION = "Update Description";
    private CreateCategoryRequestDto createCategoryRequestDto;
    private CreateCategoryRequestDto createCategoryInvalidInput;
    private CreateCategoryRequestDto updateCategoryRequestDto;
    private CategoryDto categoryDto1;
    private CategoryDto categoryDto2;
    private CategoryDto categoryDto3;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        createCategoryRequestDto = new CreateCategoryRequestDto(CATEGORY_NAME,
                CATEGORY_DESCRIPTION);
        categoryDto1 = new CategoryDto(ONE, CATEGORY_NAME_1, CATEGORY_DESCRIPTION_1);
        categoryDto2 = new CategoryDto(TWO, CATEGORY_NAME_2, CATEGORY_DESCRIPTION_2);
        categoryDto3 = new CategoryDto(THREE, CATEGORY_NAME_3, CATEGORY_DESCRIPTION_3);
        createCategoryInvalidInput = new CreateCategoryRequestDto(EMPTY_STRING,
                CATEGORY_DESCRIPTION_1);
        updateCategoryRequestDto = new CreateCategoryRequestDto(UPDATE_CATEGORY_NAME,
                UPDATE__CATEGORY_DESCRIPTION);
    }

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext
    ) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Create a new category")
    @Sql(scripts = "classpath:database/category/remove-category-from-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createCategory_ValidRequestDto_Success() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(createCategoryRequestDto);

        MvcResult result = mockMvc.perform(
                post(URL)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated()).andReturn();

        CategoryDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(), CategoryDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.id());

        EqualsBuilder.reflectionEquals(createCategoryRequestDto, actual, "id");
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Create category with invalid input name")
    @Sql(scripts = "classpath:database/category/add-category-to-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/category/remove-category-from-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createCategory_InvalidInput_StatusIsBadRequest() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(createCategoryInvalidInput);

        mockMvc.perform(post(URL)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Get all categories")
    @Sql(scripts = {
            "classpath:database/category/add-category-to-categories-table.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/category/remove-category-from-categories-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAll_GivenCategoriesInCatalog_ShouldReturnAllCategories() throws Exception {
        MvcResult result = mockMvc.perform(get(URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        JsonNode root = objectMapper.readTree(responseContent);
        JsonNode content = root.get("content");

        List<BookDto> actual = objectMapper.readerForListOf(CategoryDto.class).readValue(content);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(3, actual.size());
        EqualsBuilder.reflectionEquals(categoryDto1, actual.get(0), "id");
        EqualsBuilder.reflectionEquals(categoryDto2, actual.get(1), "id");
        EqualsBuilder.reflectionEquals(categoryDto3, actual.get(2), "id");
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Get category by id")
    @Sql(scripts = "classpath:database/category/add-category-to-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/category/remove-category-from-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getCategoryById_ValidId_ShouldReturnCategoryDto() throws Exception {
        MvcResult result = mockMvc.perform(get(URL_ID, ONE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(), CategoryDto.class);

        Assertions.assertNotNull(actual);
        EqualsBuilder.reflectionEquals(categoryDto1, actual, "id");
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Update category by id")
    @Sql(scripts = "classpath:database/category/add-category-to-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/category/remove-category-from-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateCategory_ValidInput_ShouldUpdateAndReturnCategoryDto() throws Exception {
        mockMvc.perform(get(URL_ID, ONE))
                .andExpect(status().isOk());

        String jsonRequest = objectMapper.writeValueAsString(updateCategoryRequestDto);

        MvcResult result = mockMvc.perform(put(URL_ID, ONE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                ).andExpect(status().isOk())
                .andReturn();
        CategoryDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(), CategoryDto.class);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(updateCategoryRequestDto.name(), actual.name());
        Assertions.assertEquals(updateCategoryRequestDto.description(), actual.description());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Delete category")
    @Sql(scripts = {
            "classpath:database/category/remove-category-from-categories-table.sql",
            "classpath:database/category/add-category-to-categories-table.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/category/remove-category-from-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteCategory() throws Exception {
        mockMvc.perform(get(URL_ID, ONE))
                .andExpect(status().isOk());
        mockMvc.perform(delete(URL_ID, ONE))
                .andExpect(status().isNoContent());
        mockMvc.perform(get(URL_ID, ONE))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Get all books by category return books")
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
    void getBooksByCategoryId_ValidCategoryId_ShouldReturnBooks() throws Exception {
        mockMvc.perform(get("/categories/{Id}/books", ONE)
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Get all books by category return books")
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
    void getBooksByCategoryId_InvalidId_ShouldReturnEmptyList() throws Exception {
        mockMvc.perform(get("/categories/{Id}/books", NON_EXISTENT_ID)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(0));
    }
}
