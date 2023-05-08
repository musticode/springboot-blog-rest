package com.example.springbootblogrest.service.impl;

import com.example.springbootblogrest.entity.Category;
import com.example.springbootblogrest.exception.BlogAPIException;
import com.example.springbootblogrest.exception.ResourceNotFoundException;
import com.example.springbootblogrest.payload.CategoryDto;
import com.example.springbootblogrest.repository.CategoryRepository;
import com.example.springbootblogrest.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;
    private ModelMapper modelMapper;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper){
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }


    private CategoryDto mapToDto(Category category){
        return modelMapper.map(category, CategoryDto.class);
    }

    private Category mapToEntity(CategoryDto categoryDto){
        return modelMapper.map(categoryDto, Category.class);
    }

    @Override
    public CategoryDto addCategory(CategoryDto categoryDto) {

        Category category = modelMapper.map(categoryDto, Category.class);
        Category savedCategory = categoryRepository.save(category);


        return modelMapper.map(savedCategory, CategoryDto.class);
    }

    @Override
    public CategoryDto getCategory(Long categoryId) {
//        return commentRepository.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("Comment", "id", commentId));

        Category category = categoryRepository
                .findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category","id", categoryId));

        return mapToDto(category);
    }

    @Override
    public List<CategoryDto> getAllCategories() {

        List<Category> categories = categoryRepository.findAll();

        return categories.stream().map(category -> modelMapper.map(category, CategoryDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, Long categoryId) {

        Category categoryFound = categoryRepository
                .findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category","id", categoryId));

        categoryFound.setName(categoryDto.getName());
        categoryFound.setDescription(categoryDto.getDescription());
        categoryFound.setId(categoryId);

        Category updatedCategory = categoryRepository.save(categoryFound);


        return mapToDto(updatedCategory);
    }

    @Override
    public void deleteCategory(Long categoryId) {

        // find category
        Category categoryFound = categoryRepository
                .findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category","id", categoryId));

        categoryRepository.delete(categoryFound);

    }
}
