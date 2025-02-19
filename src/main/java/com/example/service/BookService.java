package com.example.service;

import com.example.dto.BookDto;
import com.example.dto.BookSearchParameters;
import com.example.dto.CreateBookRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> findAll(Pageable pageable);

    BookDto getBookById(Long id);

    void deleteById(Long id);

    BookDto updateBookById(Long id, CreateBookRequestDto requestDto);

    public List<BookDto> search(BookSearchParameters searchParameters);
}
