package com.example.springbootblogrest.service;

import com.example.springbootblogrest.payload.PostDto;
import com.example.springbootblogrest.payload.PostResponse;

import java.util.List;

public interface PostService {

    public PostDto createPost(PostDto postDto);
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);

    public PostDto getPostByID(long id);

    public PostDto updatePost(PostDto postDto, long id);

    void deletePost(long id);

    List<PostDto> getPostsByCategory(Long categoryId);
}
