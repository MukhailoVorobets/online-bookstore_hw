package com.example.service.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
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
import com.example.util.TestConstants;
import com.example.util.TestUtil;
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
    private final TestUtil testUtil = new TestUtil();

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
        category = testUtil.getCategory();
        categoryAfterUpdate = testUtil.getCategoryAfterUpdate();
        createCategoryRequestDto = testUtil.getCreateCategoryRequestDto();
        updateDto = testUtil.getUpdateCategoryRequestDto();
        categoryDto = testUtil.getCategoryDto1();
        updatedDto = testUtil.getUpdatedDto();
        book = testUtil.getBook();
        bookDtoWithoutCategoryIds = testUtil.getBookDtoWithoutCategoryIds();
    }

    @Test
    @DisplayName("Verify all category return pageable")
    void findAll_Valid_ReturnPageableCategoryDto() {
        Pageable pageable = PageRequest.of(TestConstants.ONE.intValue(),
                TestConstants.TEN.intValue());
        Page<Category> categoryPage = new PageImpl<>(List.of(category));
        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);
        Page<CategoryDto> result = categoryService.findAll(pageable);
        assertNotNull(result);
        assertEquals(TestConstants.ONE, result.getTotalElements());
        assertEquals(TestConstants.CATEGORY_NAME,
                result.getContent().get(TestConstants.ZERO).name());
        verify(categoryRepository).findAll(pageable);
    }

    @Test
    @DisplayName("Verify category return by valid ID")
    void getCategoryDtoById_WithValidId_ShouldReturnValidCategoryDto() {
        when(categoryRepository.findById(TestConstants.ONE)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);
        CategoryDto result = categoryService.getById(TestConstants.ONE);
        assertNotNull(result);
        assertEquals(categoryDto, result);
        verify(categoryRepository).findById(TestConstants.ONE);
    }

    @Test
    @DisplayName("Throw EntityNotFoundException when category Id is not valid")
    void getCategoryDtoById_WithNotValidId_ShouldThrowEntityNotFoundException() {
        when(categoryRepository.findById(TestConstants.NON_EXISTING_ID))
                .thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> categoryService.getById(TestConstants.NON_EXISTING_ID));
        verify(categoryRepository).findById(TestConstants.NON_EXISTING_ID);
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
        verify(categoryRepository)
                .save(Mockito.any(Category.class));
        verify(categoryMapper)
                .toModel(Mockito.any(CreateCategoryRequestDto.class));
    }

    @Test
    @DisplayName("Verify updated category returns by valid input")
    void update_WithValidIdAndInput_ReturnsUpdatedCategoryDto() {
        when(categoryRepository.findById(TestConstants.ONE)).thenReturn(Optional.of(category));
        when(categoryRepository.save(Mockito.any(Category.class))).thenReturn(categoryAfterUpdate);
        when(categoryMapper.toDto(categoryAfterUpdate)).thenReturn(updatedDto);
        CategoryDto result = categoryService.update(TestConstants.ONE, updateDto);
        assertNotNull(result);
        assertEquals(TestConstants.UPDATE_CATEGORY_NAME, result.name());
        assertEquals(TestConstants.UPDATE_CATEGORY_DESCRIPTION, result.description());
        verify(categoryRepository).findById(TestConstants.ONE);
        verify(categoryRepository).save(category);
    }

    @Test
    @DisplayName("Verify category is deleted by ID")
    void deleteById_WithValidId_ShouldCallRepositoryDelete() {
        doNothing().when(categoryRepository).deleteById(TestConstants.ONE);
        categoryService.deleteById(TestConstants.ONE);
        verify(categoryRepository).deleteById(TestConstants.ONE);
    }

    @Test
    @DisplayName("Throws EntityNotFoundException when category not found by ID")
    void getCategoryById_WithInvalidId_ThrowsEntityNotFoundException() {
        when(categoryRepository.findById(TestConstants.NON_EXISTING_ID))
                .thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> categoryService.getCategoryById(TestConstants.NON_EXISTING_ID));
        verify(categoryRepository).findById(TestConstants.NON_EXISTING_ID);
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    @DisplayName("Verify book return by valid category ID")
    void getBooksByCategoryId_WithValidId_ReturnBookDtoWithoutCategoryIds() {
        Pageable pageable = PageRequest.of(TestConstants.ONE.intValue(),
                TestConstants.TEN.intValue());
        Page<Book> booksPage = new PageImpl<>(List.of(book));
        when(bookRepository.findAllByCategoryId(category.getId(), pageable)).thenReturn(booksPage);
        when(bookMapper.toDtoWithoutCategories(book)).thenReturn(bookDtoWithoutCategoryIds);
        Page<BookDtoWithoutCategoryIds> resultBookDto = categoryService
                .getBooksByCategoryId(category.getId(), pageable);
        assertNotNull(resultBookDto);
        assertEquals(bookDtoWithoutCategoryIds, resultBookDto.getContent().get(TestConstants.ZERO));
        verify(bookRepository).findAllByCategoryId(category.getId(), pageable);
    }

    @Test
    @DisplayName("Verify empty result when no books for category ID")
    void getBooksByCategoryId_WithValidIdButNoBooks_ReturnsEmptyPage() {
        Pageable pageable = PageRequest.of(TestConstants.ONE.intValue(),
                TestConstants.TEN.intValue());
        Page<Book> emptyPage = Page.empty(pageable);
        when(bookRepository.findAllByCategoryId(TestConstants.ONE, pageable)).thenReturn(emptyPage);
        Page<BookDtoWithoutCategoryIds> result = categoryService
                .getBooksByCategoryId(TestConstants.ONE, pageable);
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(bookRepository).findAllByCategoryId(TestConstants.ONE, pageable);
    }
}
