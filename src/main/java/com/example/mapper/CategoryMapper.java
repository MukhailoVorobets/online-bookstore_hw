package com.example.mapper;

import com.example.config.MapperConfig;
import com.example.dto.CategoryDto;
import com.example.dto.CreateCategoryRequestDto;
import com.example.model.Category;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class, componentModel = "spring")
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    Category toModel(CreateCategoryRequestDto requestDto);

    void updateModel(CreateCategoryRequestDto requestDto, @MappingTarget Category category);

    @Named("mapIdsToCategories")
    default Set<Category> mapIdsToCategories(Set<Long> ids) {
        if (ids == null) {
            return null;
        }
        return ids.stream().map(id -> {
            Category category = new Category();
            category.setId(id);
            return category;
        }).collect(Collectors.toSet());
    }

    @Named("mapCategoriesToIds")
    default Set<Long> mapCategoriesToIds(Set<Category> categories) {
        if (categories == null) {
            return null;
        }
        return categories.stream()
                .map(Category::getId)
                .collect(Collectors.toSet());
    }
}
