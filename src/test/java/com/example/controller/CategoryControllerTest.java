package com.example.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.dto.category.CategoryDto;
import com.example.dto.category.CreateCategoryRequestDto;
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
class CategoryControllerTest {
    protected static MockMvc mockMvc;
    private final TestUtil testUtil = new TestUtil();
    private CreateCategoryRequestDto createCategoryInvalidInput;
    private CreateCategoryRequestDto updateCategoryRequestDto;
    private CreateCategoryRequestDto createCategoryRequestDto;
    private CategoryDto categoryDtoOne;
    private CategoryDto categoryDtoTwo;
    private CategoryDto categoryDtoThree;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        createCategoryRequestDto = testUtil.getCreateCategoryRequestDto();
        categoryDtoOne = testUtil.getCategoryDtoOne();
        categoryDtoTwo = testUtil.getCategoryDtoTwo();
        categoryDtoThree = testUtil.getCategoryDtoThree();
        createCategoryInvalidInput = testUtil.getCreateCategoryInvalidInput();
        updateCategoryRequestDto = testUtil.getUpdateCategoryRequestDto();
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
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/category/remove-category-from-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createCategory_ValidRequestDto_Success() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(createCategoryRequestDto);
        MvcResult result = mockMvc.perform(post(TestConstants.URL_CATEGORY)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated()).andReturn();
        CategoryDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(), CategoryDto.class);
        assertNotNull(actual);
        assertNotNull(actual.id());
        assertEquals(TestConstants.CATEGORY_NAME, actual.name());
        assertEquals(TestConstants.CATEGORY_DESCRIPTION, actual.description());
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
        mockMvc.perform(post(TestConstants.URL_CATEGORY)
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
        MvcResult result = mockMvc.perform(get(TestConstants.URL_CATEGORY)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String responseContent = result.getResponse().getContentAsString();
        JsonNode root = objectMapper.readTree(responseContent);
        JsonNode content = root.get(TestConstants.GET_CONTENT);
        List<CategoryDto> actual = objectMapper.readerForListOf(CategoryDto.class)
                .readValue(content);
        assertNotNull(actual);
        assertEquals(3, actual.size());
        assertTrue(EqualsBuilder.reflectionEquals(categoryDtoOne,
                actual.get(TestConstants.ZERO), TestConstants.EXCLUDE_ID));
        assertTrue(EqualsBuilder.reflectionEquals(categoryDtoTwo,
                actual.get(TestConstants.ONE.intValue()), TestConstants.EXCLUDE_ID));
        assertTrue(EqualsBuilder.reflectionEquals(categoryDtoThree,
                actual.get(TestConstants.TWO.intValue()), TestConstants.EXCLUDE_ID));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Get category by id")
    @Sql(scripts = "classpath:database/category/add-category-to-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/category/remove-category-from-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getCategoryById_ValidId_ShouldReturnCategoryDto() throws Exception {
        MvcResult result = mockMvc.perform(get(TestConstants.URL_CATEGORY_ID, TestConstants.ONE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        CategoryDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(), CategoryDto.class);
        assertNotNull(actual);
        assertTrue(EqualsBuilder.reflectionEquals(categoryDtoOne,
                actual, TestConstants.EXCLUDE_ID));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Update category by id")
    @Sql(scripts = "classpath:database/category/add-category-to-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/category/remove-category-from-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateCategory_ValidInput_ShouldUpdateAndReturnCategoryDto() throws Exception {
        mockMvc.perform(get(TestConstants.URL_CATEGORY_ID, TestConstants.ONE))
                .andExpect(status().isOk());
        String jsonRequest = objectMapper.writeValueAsString(updateCategoryRequestDto);
        MvcResult result = mockMvc.perform(put(TestConstants.URL_CATEGORY_ID, TestConstants.ONE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                ).andExpect(status().isOk())
                .andReturn();
        CategoryDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(), CategoryDto.class);
        assertNotNull(actual);
        assertEquals(updateCategoryRequestDto.name(), actual.name());
        assertEquals(updateCategoryRequestDto.description(), actual.description());
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
        mockMvc.perform(get(TestConstants.URL_CATEGORY_ID, TestConstants.ONE))
                .andExpect(status().isOk());
        mockMvc.perform(delete(TestConstants.URL_CATEGORY_ID, TestConstants.ONE))
                .andExpect(status().isNoContent());
        mockMvc.perform(get(TestConstants.URL_CATEGORY_ID, TestConstants.ONE))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
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
        mockMvc.perform(get(TestConstants.URL_CATEGORY_ID_BOOKS, TestConstants.ONE)
                .param(TestConstants.PAGE, String.valueOf(TestConstants.ZERO))
                .param(TestConstants.SIZE, String.valueOf(TestConstants.TEN))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(TestConstants.CONTENT).isArray())
                .andExpect(jsonPath(TestConstants.CONTENT_LENGTH)
                        .value(TestConstants.ONE.intValue()));
    }

    @Test
    @WithMockUser
    @DisplayName("Insert an invalid category id should return an empty list")
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
        mockMvc.perform(get(TestConstants.URL_CATEGORY_ID_BOOKS, TestConstants.NON_EXISTING_ID)
                        .param(TestConstants.PAGE, String.valueOf(TestConstants.ZERO))
                        .param(TestConstants.PAGE, String.valueOf(TestConstants.TEN))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(TestConstants.CONTENT).isArray())
                .andExpect(jsonPath(TestConstants.CONTENT_LENGTH).value(TestConstants.ZERO));
    }
}
