package com.example.mapper;

import com.example.config.MapperConfig;
import com.example.dto.book.BookDto;
import com.example.dto.book.BookDtoWithoutCategoryIds;
import com.example.dto.book.CreateBookRequestDto;
import com.example.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class, componentModel = "spring", uses = CategoryMapper.class)
public interface BookMapper {
    @Mapping(target = "categoryIds", source = "categories", qualifiedByName = "mapCategoriesToIds")
    BookDto toDto(Book book);

    @Mapping(target = "categories", source = "categoryIds", qualifiedByName = "mapIdsToCategories")
    Book toModel(CreateBookRequestDto requestDto);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @Mapping(target = "categories", source = "categoryIds", qualifiedByName = "mapIdsToCategories")
    void updateModel(CreateBookRequestDto requestDto, @MappingTarget Book book);

    @Named("bookFromId")
    default Book bookFromId(Long id) {
        if (id == null) {
            return null;
        }
        Book book = new Book();
        book.setId(id);
        return book;
    }
}
