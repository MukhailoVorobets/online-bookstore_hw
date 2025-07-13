package com.example.service.book;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.example.dto.book.BookDto;
import com.example.dto.book.BookSearchParameters;
import com.example.dto.book.CreateBookRequestDto;
import com.example.exception.EntityNotFoundException;
import com.example.mapper.BookMapper;
import com.example.model.Book;
import com.example.model.Category;
import com.example.repository.book.BookRepository;
import com.example.repository.book.BookSpecificationBuilder;
import com.example.service.category.CategoryService;
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
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    private final TestUtil testUtil = new TestUtil();
    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;

    @Mock
    private CategoryService categoryService;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    private CreateBookRequestDto createBookRequestDto;
    private Book book;
    private BookDto bookDto;
    private BookDto bookUpdatedDto;
    private CreateBookRequestDto bookUpdateDto;
    private Book bookAfterUpdate;

    private Category category;

    @BeforeEach
    void setUp() {
        category = testUtil.getCategory();
        createBookRequestDto = testUtil.getCreateBookRequestDto();
        book = testUtil.getBook();
        bookDto = testUtil.getBookDto();
        bookUpdatedDto = testUtil.getBookUpdatedDto();
        bookUpdateDto = testUtil.getUpdateBookRequestDto();
        bookAfterUpdate = testUtil.getBookAfterUpdate();
    }

    @Test
    @DisplayName("Verify book save and returns Dto with valid input")
    void save_WithValidInput_ReturnBookDto() {
        when(categoryService.getCategoryById(TestConstants.ONE)).thenReturn(category);
        when(bookRepository.save(Mockito.any(Book.class))).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);
        when(bookMapper.toModel(createBookRequestDto)).thenReturn(book);
        BookDto result = bookService.save(createBookRequestDto);
        assertNotNull(result);
        assertEquals(bookDto, result);
        verify(bookRepository).save(Mockito.any(Book.class));
        verify(bookMapper).toDto(Mockito.any(Book.class));
        verify(bookMapper).toModel(Mockito.any(CreateBookRequestDto.class));
    }

    @Test
    @DisplayName("Verify all book return pageable")
    void findAll_Valid_ReturnPageableBookDto() {
        Pageable pageable = PageRequest.of(TestConstants.ONE.intValue(),
                TestConstants.TEN.intValue());
        Page<Book> booksPage = new PageImpl<>(List.of(book));
        when(bookRepository.findAll(pageable)).thenReturn(booksPage);
        when(bookMapper.toDto(book)).thenReturn(bookDto);
        Page<BookDto> result = bookService.findAll(pageable);
        assertNotNull(result);
        assertEquals(TestConstants.ONE, result.getTotalElements());
        assertEquals(bookDto, result.getContent().get(TestConstants.ZERO));
        verify(bookRepository).findAll(pageable);
    }

    @Test
    @DisplayName("Verify book return by valid ID")
    void getBookById_WithValidId_ShouldReturnValidBookDto() {
        when(bookRepository.findById(TestConstants.ONE)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookDto);
        BookDto result = bookService.getBookById(TestConstants.ONE);
        assertNotNull(result);
        assertEquals(bookDto, result);
        verify(bookRepository).findById(TestConstants.ONE);
    }

    @Test
    @DisplayName("Verify category is deleted by ID")
    void deleteById_WithValidId_ShouldCallRepositoryDelete() {
        doNothing().when(bookRepository).deleteById(TestConstants.ONE);
        bookService.deleteById(TestConstants.ONE);
        verify(bookRepository).deleteById(TestConstants.ONE);
        verify(bookRepository).deleteById(TestConstants.ONE);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    @DisplayName("Verify updated book returns by valid input")
    void updateBookById_WithValidIdAndInput_ReturnsUpdatedBookDto() {
        when(bookRepository.findById(TestConstants.ONE)).thenReturn(Optional.of(book));
        when(bookRepository.save(Mockito.any(Book.class))).thenReturn(bookAfterUpdate);
        when(bookMapper.toDto(bookAfterUpdate)).thenReturn(bookUpdatedDto);
        BookDto result = bookService.updateBookById(TestConstants.ONE, bookUpdateDto);
        assertNotNull(result);
        assertEquals(bookUpdatedDto, result);
        verify(bookRepository).findById(TestConstants.ONE);
        verify(bookRepository).save(Mockito.any(Book.class));
        verify(bookMapper)
                .toDto(Mockito.any(Book.class));
    }

    @Test
    @DisplayName("Throw EntityNotFoundException when get non-existing book")
    void getBookById_WithInvalidIdAndInput_ThrowsEntityNotFoundException() {
        when(bookRepository.findById(TestConstants.NON_EXISTING_ID))
                .thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> bookService.getBookById(TestConstants.NON_EXISTING_ID));
        verify(bookRepository).findById(TestConstants.NON_EXISTING_ID);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    @DisplayName("Verify search returns mapped BookDto Page from Specification")
    void search_WithValidParameters_ReturnsBookDtoPage() {
        BookSearchParameters searchParameters = new BookSearchParameters(
                new String[] {TestConstants.BOOK_TITLE},
                new String[] {TestConstants.BOOK_AUTHOR},
                new String[] {TestConstants.BOOK_ISBN});
        Pageable pageable = PageRequest.of(TestConstants.ZERO, TestConstants.TEN.intValue());
        Specification<Book> specification = mock(Specification.class);
        Page<Book> bookPage = new PageImpl<>(List.of(book));
        when(bookSpecificationBuilder.build(searchParameters)).thenReturn(specification);
        when(bookRepository.findAll(specification, pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(book)).thenReturn(bookDto);
        Page<BookDto> result = bookService.search(searchParameters, pageable);
        assertNotNull(result);
        assertEquals(TestConstants.ONE, result.getTotalElements());
        assertEquals(bookDto, result.getContent().get(TestConstants.ZERO));
    }
}
