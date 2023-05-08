package com.example.springbootblogrest.service.impl;

import com.example.springbootblogrest.entity.Category;
import com.example.springbootblogrest.entity.Post;
import com.example.springbootblogrest.exception.ResourceNotFoundException;
import com.example.springbootblogrest.payload.PostDto;
import com.example.springbootblogrest.payload.PostResponse;
import com.example.springbootblogrest.repository.CategoryRepository;
import com.example.springbootblogrest.repository.PostRepository;
import com.example.springbootblogrest.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;
    private ModelMapper mapper;
    private CategoryRepository categoryRepository;


    @Autowired
    public PostServiceImpl(PostRepository postRepository,
                           ModelMapper mapper,
                           CategoryRepository categoryRepository){
        this.postRepository = postRepository;
        this.mapper = mapper;
        this.categoryRepository = categoryRepository;
    }

    // convert entity into DTO
    private PostDto mapToDto(Post post){

        PostDto postDtoMapper = mapper.map(post, PostDto.class);


        /*
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setTitle(post.getTitle());
        postDto.setDescription(post.getDescription());
        postDto.setContent(post.getContent());

         */

        return postDtoMapper;
    }


    private Post mapToEntity(PostDto postDto){

        Post postMap = mapper.map(postDto, Post.class);

        /*
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setDescription(postDto.getDescription());

         */

        return postMap;

    }

    @Override
    public PostDto updatePost(PostDto postDto, long id) {


        // get post by id from the database
        Post post = postRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Post", "id", id));

        // post's category
        Category category = categoryRepository.findById(postDto.getCategoryId()).orElseThrow(()-> new ResourceNotFoundException("Category", "id", postDto.getCategoryId()));



        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());
        post.setCategory(category);

        Post updatedPost = postRepository.save(post);

        return mapToDto(updatedPost);
    }

    @Override
    public void deletePost(long id) {
        Post post = postRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Post", "id", id));
        postRepository.delete(post);
    }

    @Override
    public List<PostDto> getPostsByCategory(Long categoryId) {


        Category category = categoryRepository
                .findById(categoryId)
                .orElseThrow(()->
                        new ResourceNotFoundException("Category", "id", categoryId));

        List<Post> posts = postRepository.findByCategoryId(categoryId);



        return posts
                .stream()
                .map((post) ->
                        mapToDto(post)).collect(Collectors.toList());
    }


    @Override
    public PostDto getPostByID(long id) {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post","id", id));

        return mapToDto(post);
    }


    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {

        //sort direction
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();


        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Post> postList = postRepository.findAll(pageable);

        List<Post> listOfPosts = postList.getContent();

        List<PostDto> content = listOfPosts.stream().map(post -> mapToDto(post)).collect(Collectors.toList());
        PostResponse postResponse = new PostResponse();
        postResponse.setContent(content);
        postResponse.setPageNo(postList.getNumber());
        postResponse.setPageSize(postList.getSize());
        postResponse.setTotalElement(postList.getTotalElements());
        postResponse.setTotalPages(postList.getTotalPages());
        postResponse.setLast(postList.isLast());

        return postResponse;

        //return listOfPosts.stream().map(post -> mapToDto(post)).collect(Collectors.toList());


        /**       map to dto for every post
         *
         *        List<Post> postList = postRepository.findAll();
         *        List<PostDto> postDtoList = new ArrayList<>();
         *        for (int i = 0; i < postList.size(); i++) {
         *             postDtoList.add(mapToDto(postList.get(i)));
         *         }
         *
         *       return postDtoList;
         *
         * */


    }




    @Override
    public PostDto createPost(PostDto postDto) {

        Category category = categoryRepository.findById(postDto.getCategoryId())
                .orElseThrow(()->
                        new ResourceNotFoundException("Category", "id", postDto.getCategoryId()));





        //mapping
        Post post = mapToEntity(postDto);
        post.setCategory(category);

        // add to database
        Post newPost = postRepository.save(post);

        // convert entity to DTO
        PostDto postResponse = mapToDto(newPost);

        return postResponse;
    }


}
