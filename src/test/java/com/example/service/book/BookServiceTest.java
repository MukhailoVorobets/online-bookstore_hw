package com.example.service.book;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.dto.book.BookDto;
import com.example.dto.book.BookSearchParameters;
import com.example.dto.book.CreateBookRequestDto;
import com.example.exception.EntityNotFoundException;
import com.example.mapper.BookMapper;
import com.example.model.Book;
import com.example.model.Category;
import com.example.repository.book.BookRepository;
import com.example.repository.book.BookSpecificationBuilder;
import com.example.repository.category.CategoryRepository;
import com.example.service.category.CategoryService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
    private static final int METHOD_CALLED_ONCE = 1;
    private static final Long NON_EXISTING_ID = 999L;
    private static final Long ONE = 1L;
    private static final int ZERO = 0;
    private static final int TEN = 10;
    private static final String CATEGORY_NAME = "Category";
    private static final String CATEGORY_DESCRIPTION = "Category description";
    private static final String BOOK_TITLE = "Title";
    private static final String BOOK_AUTHOR = "Author";
    private static final String BOOK_ISBN = "ISBN123456789";
    private static final BigDecimal BOOK_PRICE = BigDecimal.valueOf(99.0);
    private static final String BOOK_DESCRIPTION = "Description test";
    private static final String BOOK_COVER_IMAGE = "https://example.com/tast-cover-image.jpg";

    private static final String UPDATE_BOOK_TITLE = "Update Title";
    private static final String UPDATE_BOOK_AUTHOR = "Update Author";
    private static final BigDecimal UPDATE_BOOK_PRICE = BigDecimal.valueOf(99.9);
    private static final String UPDATE_BOOK_DESCRIPTION = "Update Description test";
    private static final String UPDATE_BOOK_COVER_IMAGE = "https://example.com/tast-cover-image-update.jpg";
    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;

    @Mock
    private CategoryRepository categoryRepository;

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
        category = new Category();
        category.setId(ONE);
        category.setName(CATEGORY_NAME);
        category.setDescription(CATEGORY_DESCRIPTION);

        createBookRequestDto = new CreateBookRequestDto();
        createBookRequestDto.setTitle(BOOK_TITLE);
        createBookRequestDto.setAuthor(BOOK_AUTHOR);
        createBookRequestDto.setIsbn(BOOK_ISBN);
        createBookRequestDto.setPrice(BOOK_PRICE);
        createBookRequestDto.setDescription(BOOK_DESCRIPTION);
        createBookRequestDto.setCoverImage(BOOK_COVER_IMAGE);
        createBookRequestDto.setCategoryIds(Set.of(ONE));

        book = new Book();
        book.setId(ONE);
        book.setTitle(BOOK_TITLE);
        book.setAuthor(BOOK_AUTHOR);
        book.setIsbn(BOOK_ISBN);
        book.setPrice(BOOK_PRICE);
        book.setDescription(BOOK_DESCRIPTION);
        book.setCoverImage(BOOK_COVER_IMAGE);

        bookDto = new BookDto();
        bookDto.setId(ONE);
        bookDto.setTitle(BOOK_TITLE);
        bookDto.setAuthor(BOOK_AUTHOR);
        bookDto.setIsbn(BOOK_ISBN);
        bookDto.setPrice(BOOK_PRICE);
        bookDto.setDescription(BOOK_DESCRIPTION);
        bookDto.setCoverImage(BOOK_COVER_IMAGE);
        bookDto.setCategoryIds(Set.of(ONE));

        bookUpdatedDto = new BookDto();
        bookUpdatedDto.setId(ONE);
        bookUpdatedDto.setTitle(UPDATE_BOOK_TITLE);
        bookUpdatedDto.setAuthor(UPDATE_BOOK_AUTHOR);
        bookUpdatedDto.setIsbn(BOOK_ISBN);
        bookUpdatedDto.setPrice(UPDATE_BOOK_PRICE);
        bookUpdatedDto.setDescription(UPDATE_BOOK_DESCRIPTION);
        bookUpdatedDto.setCoverImage(UPDATE_BOOK_COVER_IMAGE);
        bookUpdatedDto.setCategoryIds(Set.of(ONE));

        bookUpdateDto = new CreateBookRequestDto();
        bookUpdateDto.setTitle(UPDATE_BOOK_TITLE);
        bookUpdateDto.setAuthor(UPDATE_BOOK_AUTHOR);
        bookUpdateDto.setIsbn(BOOK_ISBN);
        bookUpdateDto.setPrice(UPDATE_BOOK_PRICE);
        bookUpdateDto.setDescription(UPDATE_BOOK_DESCRIPTION);
        bookUpdateDto.setCoverImage(UPDATE_BOOK_COVER_IMAGE);
        bookUpdateDto.setCategoryIds(Set.of(ONE));

        bookAfterUpdate = new Book();
        bookAfterUpdate.setId(ONE);
        bookAfterUpdate.setTitle(UPDATE_BOOK_TITLE);
        bookAfterUpdate.setAuthor(UPDATE_BOOK_AUTHOR);
        bookAfterUpdate.setIsbn(BOOK_ISBN);
        bookAfterUpdate.setPrice(UPDATE_BOOK_PRICE);
        bookAfterUpdate.setDescription(UPDATE_BOOK_DESCRIPTION);
        bookAfterUpdate.setCoverImage(UPDATE_BOOK_COVER_IMAGE);
    }

    @Test
    @DisplayName("Verify book save and returns Dto with valid input")
    void save_WithValidInput_ReturnBookDto() {
        Mockito.when(categoryService.getCategoryById(ONE)).thenReturn(category);
        Mockito.when(bookRepository.save(Mockito.any(Book.class))).thenReturn(book);
        Mockito.when(bookMapper.toDto(book)).thenReturn(bookDto);
        Mockito.when(bookMapper.toModel(createBookRequestDto)).thenReturn(book);

        BookDto result = bookService.save(createBookRequestDto);

        assertNotNull(result);
        assertEquals(bookDto, result);

        Mockito.verify(bookRepository, Mockito.times(METHOD_CALLED_ONCE))
                .save(Mockito.any(Book.class));
        Mockito.verify(bookMapper, Mockito.times(METHOD_CALLED_ONCE))
                .toDto(Mockito.any(Book.class));
        Mockito.verify(bookMapper, Mockito.times(METHOD_CALLED_ONCE))
                .toModel(Mockito.any(CreateBookRequestDto.class));
    }

    @Test
    @DisplayName("Verify all book return pageable")
    void findAll_Valid_ReturnPageableBookDto() {
        Pageable pageable = PageRequest.of(ONE.intValue(), TEN);
        Page<Book> booksPage = new PageImpl<>(List.of(book));

        Mockito.when(bookRepository.findAll(pageable)).thenReturn(booksPage);
        Mockito.when(bookMapper.toDto(book)).thenReturn(bookDto);

        Page<BookDto> result = bookService.findAll(pageable);

        assertNotNull(result);
        assertEquals(ONE, result.getTotalElements());
        assertEquals(bookDto, result.getContent().get(ZERO));

        Mockito.verify(bookRepository, Mockito.times(METHOD_CALLED_ONCE)).findAll(pageable);
    }

    @Test
    @DisplayName("Verify book return by valid ID")
    void getBookById_WithValidId_ShouldReturnValidBookDto() {
        Mockito.when(bookRepository.findById(ONE)).thenReturn(Optional.of(book));
        Mockito.when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto result = bookService.getBookById(ONE);

        assertNotNull(result);
        assertEquals(bookDto, result);

        Mockito.verify(bookRepository, Mockito.times(METHOD_CALLED_ONCE)).findById(ONE);
    }

    @Test
    @DisplayName("Verify category is deleted by ID")
    void deleteById_WithValidId_ShouldCallRepositoryDelete() {
        Mockito.doNothing().when(bookRepository).deleteById(ONE);

        bookService.deleteById(ONE);

        Mockito.verify(bookRepository).deleteById(ONE);

        Mockito.verify(bookRepository).deleteById(ONE);
        Mockito.verifyNoMoreInteractions(bookRepository);
    }

    @Test
    @DisplayName("Verify updated book returns by valid input")
    void updateBookById_WithValidIdAndInput_ReturnsUpdatedBookDto() {
        Mockito.when(bookRepository.findById(ONE)).thenReturn(Optional.of(book));
        Mockito.when(bookRepository.save(Mockito.any(Book.class))).thenReturn(bookAfterUpdate);
        Mockito.when(bookMapper.toDto(bookAfterUpdate)).thenReturn(bookUpdatedDto);

        BookDto result = bookService.updateBookById(ONE, bookUpdateDto);

        assertNotNull(result);
        assertEquals(bookUpdatedDto, result);

        Mockito.verify(bookRepository, Mockito.times(METHOD_CALLED_ONCE)).findById(ONE);
        Mockito.verify(bookRepository, Mockito.times(METHOD_CALLED_ONCE))
                .save(Mockito.any(Book.class));
        Mockito.verify(bookMapper, Mockito.times(METHOD_CALLED_ONCE))
                .toDto(Mockito.any(Book.class));
    }

    @Test
    @DisplayName("Throw EntityNotFoundException when get non-existing book")
    void getBookById_WithInvalidIdAndInput_ThrowsEntityNotFoundException() {
        Mockito.when(bookRepository.findById(NON_EXISTING_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> bookService.getBookById(NON_EXISTING_ID));

        Mockito.verify(bookRepository, Mockito.times(METHOD_CALLED_ONCE)).findById(NON_EXISTING_ID);
        Mockito.verifyNoMoreInteractions(bookRepository);
    }

    @Test
    @DisplayName("Verify search returns mapped BookDto Page from Specification")
    void search_WithValidParameters_ReturnsBookDtoPage() {
        BookSearchParameters searchParameters = new BookSearchParameters(
                new String[] {BOOK_TITLE}, new String[] {BOOK_AUTHOR}, new String[] {BOOK_ISBN});
        Pageable pageable = PageRequest.of(ZERO, TEN);

        Specification<Book> specification = Mockito.mock(Specification.class);

        Page<Book> bookPage = new PageImpl<>(List.of(book));

        Mockito.when(bookSpecificationBuilder.build(searchParameters)).thenReturn(specification);
        Mockito.when(bookRepository.findAll(specification, pageable)).thenReturn(bookPage);
        Mockito.when(bookMapper.toDto(book)).thenReturn(bookDto);

        Page<BookDto> result = bookService.search(searchParameters, pageable);

        assertNotNull(result);
        assertEquals(ONE, result.getTotalElements());
        assertEquals(bookDto, result.getContent().get(ZERO));
    }
}
