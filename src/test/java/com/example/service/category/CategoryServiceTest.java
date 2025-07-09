package com.example.service.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.example.dto.book.BookDtoWithoutCategoryIds;
import com.example.dto.category.CategoryDto;
import com.example.dto.category.CreateCategoryRequestDto;
import com.example.exception.EntityNotFoundException;
import com.example.mapper.BookMapper;
import com.example.mapper.CategoryMapper;
import com.example.model.Book;
import com.example.model.Category;
import com.example.repository.book.BookRepository;
import com.example.repository.category.CategoryRepository;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    private static final int METHOD_CALLED_ONCE = 1;
    private static final Long ONE = 1L;
    private static final Long NON_EXISTING_ID = 999L;
    private static final int TEN = 10;
    private static final int ZERO = 0;
    private static final String CATEGORY_NAME = "Category";
    private static final String CATEGORY_DESCRIPTION = "Description";
    private static final String UPDATE_CATEGORY_NAME = "Update name";
    private static final String UPDATE_CATEGORY_DESCRIPTION = "Update description";
    private static final String BOOK_TITLE = "Title";
    private static final String BOOK_AUTHOR = "Author";
    private static final String BOOK_ISBN = "ISBN123456789";
    private static final BigDecimal BOOK_PRICE = BigDecimal.valueOf(99.0);
    private static final String BOOK_DESCRIPTION = "Description test";
    private static final String BOOK_COVER_IMAGE = "https://example.com/tast-cover-image.jpg";

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Book book;
    private BookDtoWithoutCategoryIds bookDtoWithoutCategoryIds;
    private Category category;
    private Category categoryAfterUpdate;
    private CreateCategoryRequestDto createCategoryRequestDto;
    private CreateCategoryRequestDto updateDto;
    private CategoryDto categoryDto;
    private CategoryDto updatedDto;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(ONE);
        category.setName(CATEGORY_NAME);
        category.setDescription(CATEGORY_DESCRIPTION);

        categoryAfterUpdate = new Category();
        categoryAfterUpdate.setId(ONE);
        categoryAfterUpdate.setName(UPDATE_CATEGORY_NAME);
        categoryAfterUpdate.setDescription(UPDATE_CATEGORY_DESCRIPTION);

        createCategoryRequestDto = new CreateCategoryRequestDto(
                CATEGORY_NAME,
                CATEGORY_DESCRIPTION
        );

        updateDto = new CreateCategoryRequestDto(
                UPDATE_CATEGORY_NAME,
                UPDATE_CATEGORY_DESCRIPTION
        );

        categoryDto = new CategoryDto(
                ONE,
                CATEGORY_NAME,
                CATEGORY_DESCRIPTION
        );

        updatedDto = new CategoryDto(
                ONE,
                UPDATE_CATEGORY_NAME,
                UPDATE_CATEGORY_DESCRIPTION
        );

        book = new Book();
        book.setId(ONE);
        book.setTitle(BOOK_TITLE);
        book.setAuthor(BOOK_AUTHOR);
        book.setIsbn(BOOK_ISBN);
        book.setPrice(BOOK_PRICE);
        book.setDescription(BOOK_DESCRIPTION);
        book.setCoverImage(BOOK_COVER_IMAGE);
        book.setCategories(Collections.singleton(category));

        bookDtoWithoutCategoryIds = new BookDtoWithoutCategoryIds(
                ONE,
                BOOK_TITLE,
                BOOK_AUTHOR,
                BOOK_ISBN,
                BOOK_PRICE,
                BOOK_DESCRIPTION,
                BOOK_COVER_IMAGE
        );

    }

    @Test
    @DisplayName("Verify all category return pageable")
    void findAll_Valid_ReturnPageableCategoryDto() {
        Pageable pageable = PageRequest.of(ONE.intValue(), TEN);
        Page<Category> categoryPage = new PageImpl<>(List.of(category));

        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        Page<CategoryDto> result = categoryService.findAll(pageable);

        assertNotNull(result);
        assertEquals(ONE, result.getTotalElements());
        assertEquals(CATEGORY_NAME, result.getContent().get(ZERO).name());

        verify(categoryRepository, Mockito.times(METHOD_CALLED_ONCE)).findAll(pageable);
    }

    @Test
    @DisplayName("Verify category return by valid ID")
    void getCategoryDtoById_WithValidId_ShouldReturnValidCategoryDto() {
        when(categoryRepository.findById(ONE)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto result = categoryService.getById(ONE);

        assertNotNull(result);
        assertEquals(categoryDto, result);
        verify(categoryRepository, Mockito.times(METHOD_CALLED_ONCE)).findById(ONE);
    }

    @Test
    @DisplayName("Throw EntityNotFoundException when category Id is not valid")
    void getCategoryDtoById_WithNotValidId_ShouldThrowEntityNotFoundException() {
        when(categoryRepository.findById(NON_EXISTING_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> categoryService.getById(NON_EXISTING_ID));
        verify(categoryRepository, Mockito.times(METHOD_CALLED_ONCE)).findById(NON_EXISTING_ID);
    }

    @Test
    @DisplayName("Verify category save and returns Dto with valid input")
    void save_WithValidInput_ReturnCategoryDto() {
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);
        when(categoryMapper.toModel(createCategoryRequestDto)).thenReturn(category);

        CategoryDto result = categoryService.save(createCategoryRequestDto);

        assertNotNull(result);
        assertEquals(categoryDto, result);

        verify(categoryRepository, Mockito.times(METHOD_CALLED_ONCE))
                .save(Mockito.any(Category.class));
        verify(categoryMapper, Mockito.times(METHOD_CALLED_ONCE))
                .toModel(Mockito.any(CreateCategoryRequestDto.class));
    }

    @Test
    @DisplayName("Verify updated category returns by valid input")
    void update_WithValidIdAndInput_ReturnsUpdatedCategoryDto() {
        when(categoryRepository.findById(ONE)).thenReturn(Optional.of(category));
        when(categoryRepository.save(Mockito.any(Category.class))).thenReturn(categoryAfterUpdate);
        when(categoryMapper.toDto(categoryAfterUpdate)).thenReturn(updatedDto);

        CategoryDto result = categoryService.update(ONE, updateDto);

        assertNotNull(result);
        assertEquals(UPDATE_CATEGORY_NAME, result.name());
        assertEquals(UPDATE_CATEGORY_DESCRIPTION, result.description());

        verify(categoryRepository, Mockito.times(METHOD_CALLED_ONCE)).findById(ONE);
        verify(categoryRepository, Mockito.times(METHOD_CALLED_ONCE)).save(category);
    }

    @Test
    @DisplayName("Verify category is deleted by ID")
    void deleteById_WithValidId_ShouldCallRepositoryDelete() {
        Mockito.doNothing().when(categoryRepository).deleteById(ONE);

        categoryService.deleteById(ONE);

        verify(categoryRepository).deleteById(ONE);
    }

    @Test
    @DisplayName("Throws EntityNotFoundException when category not found by ID")
    void getCategoryById_WithInvalidId_ThrowsEntityNotFoundException() {
        when(categoryRepository.findById(NON_EXISTING_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> categoryService.getCategoryById(NON_EXISTING_ID));

        verify(categoryRepository, times(1)).findById(NON_EXISTING_ID);
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    @DisplayName("Verify book return by valid category ID")
    void getBooksByCategoryId_WithValidId_ReturnBookDtoWithoutCategoryIds() {
        Pageable pageable = PageRequest.of(ONE.intValue(), TEN);
        Page<Book> booksPage = new PageImpl<>(List.of(book));

        when(bookRepository.findAllByCategoryId(category.getId(), pageable)).thenReturn(booksPage);
        when(bookMapper.toDtoWithoutCategories(book)).thenReturn(bookDtoWithoutCategoryIds);

        Page<BookDtoWithoutCategoryIds> resultBookDto = categoryService
                .getBooksByCategoryId(category.getId(), pageable);

        assertNotNull(resultBookDto);
        assertEquals(bookDtoWithoutCategoryIds, resultBookDto.getContent().get(ZERO));

        verify(bookRepository, Mockito.times(METHOD_CALLED_ONCE))
                .findAllByCategoryId(category.getId(), pageable);
    }

    @Test
    @DisplayName("Verify empty result when no books for category ID")
    void getBooksByCategoryId_WithValidIdButNoBooks_ReturnsEmptyPage() {
        Pageable pageable = PageRequest.of(ONE.intValue(), TEN);
        Page<Book> emptyPage = Page.empty(pageable);

        when(bookRepository.findAllByCategoryId(ONE, pageable)).thenReturn(emptyPage);

        Page<BookDtoWithoutCategoryIds> result = categoryService
                .getBooksByCategoryId(ONE, pageable);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(bookRepository).findAllByCategoryId(ONE, pageable);
    }
}
