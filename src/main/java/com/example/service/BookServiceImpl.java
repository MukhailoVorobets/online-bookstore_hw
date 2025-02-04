package com.example.service;

import com.example.dto.BookDto;
import com.example.dto.CreateBookRequestDto;
import com.example.exception.EntityNotFoundException;
import com.example.mapper.BookMapper;
import com.example.model.Book;
import com.example.repository.BookRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findByIsDeletedFalse().stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .filter(b -> !b.isDeleted())
                .orElseThrow(() -> new EntityNotFoundException("Can't not find book by id " + id));
        return bookMapper.toDto(book);
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public BookDto updateBookById(Long id, CreateBookRequestDto requestDto) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't not find book by id " + id));
        bookMapper.updateModel(requestDto, book);
        return bookMapper.toDto(bookRepository.save(book));
    }
}
