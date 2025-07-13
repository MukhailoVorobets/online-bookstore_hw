package com.example.util;

import com.example.dto.book.BookDto;
import com.example.dto.book.BookDtoWithoutCategoryIds;
import com.example.dto.book.CreateBookRequestDto;
import com.example.dto.category.CategoryDto;
import com.example.dto.category.CreateCategoryRequestDto;
import com.example.model.Book;
import com.example.model.Category;
import java.util.Collections;
import java.util.Set;

public class TestUtil {
    public Category getCategory() {
        Category category = new Category();
        category.setId(TestConstants.ONE);
        category.setName(TestConstants.CATEGORY_NAME);
        category.setDescription(TestConstants.CATEGORY_DESCRIPTION);
        return category;
    }

    public Category getCategoryAfterUpdate() {
        Category categoryAfterUpdate = new Category();
        categoryAfterUpdate.setId(TestConstants.ONE);
        categoryAfterUpdate.setName(TestConstants.UPDATE_CATEGORY_NAME);
        categoryAfterUpdate.setDescription(TestConstants.UPDATE_CATEGORY_DESCRIPTION);
        return categoryAfterUpdate;
    }

    public CreateCategoryRequestDto getCreateCategoryRequestDto() {
        return new CreateCategoryRequestDto(
                TestConstants.CATEGORY_NAME,
                TestConstants.CATEGORY_DESCRIPTION);
    }

    public CategoryDto getUpdatedDto() {
        return new CategoryDto(
                TestConstants.ONE,
                TestConstants.UPDATE_CATEGORY_NAME,
                TestConstants.UPDATE_CATEGORY_DESCRIPTION);
    }

    public CategoryDto getCategoryDto1() {
        return new CategoryDto(
                TestConstants.ONE,
                TestConstants.CATEGORY_NAME_1,
                TestConstants.CATEGORY_DESCRIPTION_1);
    }

    public CategoryDto getCategoryDto2() {
        return new CategoryDto(
                TestConstants.TWO,
                TestConstants.CATEGORY_NAME_2,
                TestConstants.CATEGORY_DESCRIPTION_2);
    }

    public CategoryDto getCategoryDto3() {
        return new CategoryDto(
                TestConstants.THREE,
                TestConstants.CATEGORY_NAME_3,
                TestConstants.CATEGORY_DESCRIPTION_3);
    }

    public CreateCategoryRequestDto getCreateCategoryInvalidInput() {
        return new CreateCategoryRequestDto(
                TestConstants.EMPTY_STRING,
                TestConstants.CATEGORY_DESCRIPTION);
    }

    public CreateCategoryRequestDto getUpdateCategoryRequestDto() {
        return new CreateCategoryRequestDto(
                TestConstants.UPDATE_CATEGORY_NAME,
                TestConstants.UPDATE_CATEGORY_DESCRIPTION);
    }

    public Book getBook() {
        Book book = new Book();
        book.setId(TestConstants.ONE);
        book.setTitle(TestConstants.BOOK_TITLE);
        book.setAuthor(TestConstants.BOOK_AUTHOR);
        book.setIsbn(TestConstants.BOOK_ISBN);
        book.setPrice(TestConstants.BOOK_PRICE);
        book.setDescription(TestConstants.BOOK_DESCRIPTION);
        book.setCoverImage(TestConstants.BOOK_COVER_IMAGE);
        book.setCategories(Collections.singleton(getCategory()));
        return book;
    }

    public BookDtoWithoutCategoryIds getBookDtoWithoutCategoryIds() {
        return new BookDtoWithoutCategoryIds(
                TestConstants.ONE,
                TestConstants.BOOK_TITLE,
                TestConstants.BOOK_AUTHOR,
                TestConstants.BOOK_ISBN,
                TestConstants.BOOK_PRICE,
                TestConstants.BOOK_DESCRIPTION,
                TestConstants.BOOK_COVER_IMAGE
        );
    }

    public CreateBookRequestDto getCreateBookRequestDto() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle(TestConstants.BOOK_TITLE_1);
        requestDto.setAuthor(TestConstants.BOOK_AUTHOR_1);
        requestDto.setIsbn(TestConstants.BOOK_ISBN_1);
        requestDto.setPrice(TestConstants.BOOK_PRICE_1);
        requestDto.setDescription(TestConstants.BOOK_DESCRIPTION_1);
        requestDto.setCoverImage(TestConstants.BOOK_COVER_IMAGE_1);
        requestDto.setCategoryIds(Set.of(TestConstants.ONE));
        return requestDto;
    }

    public BookDto getBookDto() {
        BookDto bookDto1 = new BookDto();
        bookDto1.setId(TestConstants.ONE);
        bookDto1.setTitle(TestConstants.BOOK_TITLE);
        bookDto1.setAuthor(TestConstants.BOOK_AUTHOR);
        bookDto1.setIsbn(TestConstants.BOOK_ISBN);
        bookDto1.setPrice(TestConstants.BOOK_PRICE);
        bookDto1.setDescription(TestConstants.BOOK_DESCRIPTION);
        bookDto1.setCoverImage(TestConstants.BOOK_COVER_IMAGE);
        bookDto1.setCategoryIds(Set.of(TestConstants.ONE));
        return bookDto1;
    }

    public BookDto getBookDto1() {
        BookDto bookDto1 = new BookDto();
        bookDto1.setId(TestConstants.ONE);
        bookDto1.setTitle(TestConstants.BOOK_TITLE_1);
        bookDto1.setAuthor(TestConstants.BOOK_AUTHOR_1);
        bookDto1.setIsbn(TestConstants.BOOK_ISBN_1);
        bookDto1.setPrice(TestConstants.BOOK_PRICE_1);
        bookDto1.setDescription(TestConstants.BOOK_DESCRIPTION_1);
        bookDto1.setCoverImage(TestConstants.BOOK_COVER_IMAGE_1);
        bookDto1.setCategoryIds(Set.of(TestConstants.ONE));
        return bookDto1;
    }

    public BookDto getBookDto2() {
        BookDto bookDto2 = new BookDto();
        bookDto2.setId(TestConstants.TWO);
        bookDto2.setTitle(TestConstants.BOOK_TITLE_2);
        bookDto2.setAuthor(TestConstants.BOOK_AUTHOR_2);
        bookDto2.setIsbn(TestConstants.BOOK_ISBN_2);
        bookDto2.setPrice(TestConstants.BOOK_PRICE_2);
        bookDto2.setDescription(TestConstants.BOOK_DESCRIPTION_2);
        bookDto2.setCoverImage(TestConstants.BOOK_COVER_IMAGE_2);
        bookDto2.setCategoryIds(Set.of(TestConstants.TWO));
        return bookDto2;
    }

    public BookDto getBookDto3() {
        BookDto bookDto3 = new BookDto();
        bookDto3.setId(TestConstants.THREE);
        bookDto3.setTitle(TestConstants.BOOK_TITLE_3);
        bookDto3.setAuthor(TestConstants.BOOK_AUTHOR_3);
        bookDto3.setIsbn(TestConstants.BOOK_ISBN_3);
        bookDto3.setPrice(TestConstants.BOOK_PRICE_3);
        bookDto3.setDescription(TestConstants.BOOK_DESCRIPTION_3);
        bookDto3.setCoverImage(TestConstants.BOOK_COVER_IMAGE_3);
        bookDto3.setCategoryIds(Set.of(TestConstants.THREE));
        return bookDto3;
    }

    public CreateBookRequestDto getUpdateBookRequestDto() {
        CreateBookRequestDto updateRequestDto = new CreateBookRequestDto();
        updateRequestDto.setTitle(TestConstants.UPDATE_BOOK_TITLE);
        updateRequestDto.setAuthor(TestConstants.UPDATE_BOOK_AUTHOR);
        updateRequestDto.setIsbn(TestConstants.UPDATE_BOOK_ISBN);
        updateRequestDto.setPrice(TestConstants.UPDATE_BOOK_PRICE);
        updateRequestDto.setDescription(TestConstants.UPDATE_BOOK_DESCRIPTION);
        updateRequestDto.setCoverImage(TestConstants.UPDATE_BOOK_COVER_IMAGE);
        updateRequestDto.setCategoryIds(Set.of(TestConstants.TWO));
        return updateRequestDto;
    }

    public BookDto getBookUpdatedDto() {
        BookDto bookUpdatedDto = new BookDto();
        bookUpdatedDto.setId(TestConstants.ONE);
        bookUpdatedDto.setTitle(TestConstants.UPDATE_BOOK_TITLE);
        bookUpdatedDto.setAuthor(TestConstants.UPDATE_BOOK_AUTHOR);
        bookUpdatedDto.setIsbn(TestConstants.BOOK_ISBN);
        bookUpdatedDto.setPrice(TestConstants.UPDATE_BOOK_PRICE);
        bookUpdatedDto.setDescription(TestConstants.UPDATE_BOOK_DESCRIPTION);
        bookUpdatedDto.setCoverImage(TestConstants.UPDATE_BOOK_COVER_IMAGE);
        bookUpdatedDto.setCategoryIds(Set.of(TestConstants.ONE));
        return bookUpdatedDto;
    }

    public Book getBookAfterUpdate() {
        Book bookAfterUpdate = new Book();
        bookAfterUpdate.setId(TestConstants.ONE);
        bookAfterUpdate.setTitle(TestConstants.UPDATE_BOOK_TITLE);
        bookAfterUpdate.setAuthor(TestConstants.UPDATE_BOOK_AUTHOR);
        bookAfterUpdate.setIsbn(TestConstants.BOOK_ISBN);
        bookAfterUpdate.setPrice(TestConstants.UPDATE_BOOK_PRICE);
        bookAfterUpdate.setDescription(TestConstants.UPDATE_BOOK_DESCRIPTION);
        bookAfterUpdate.setCoverImage(TestConstants.UPDATE_BOOK_COVER_IMAGE);
        return bookAfterUpdate;
    }
}
