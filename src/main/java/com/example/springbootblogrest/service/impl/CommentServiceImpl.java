package com.example.springbootblogrest.service.impl;

import com.example.springbootblogrest.entity.Comment;
import com.example.springbootblogrest.entity.Post;
import com.example.springbootblogrest.exception.BlogAPIException;
import com.example.springbootblogrest.exception.ResourceNotFoundException;
import com.example.springbootblogrest.payload.CommentDto;
import com.example.springbootblogrest.repository.CommentRepository;
import com.example.springbootblogrest.repository.PostRepository;
import com.example.springbootblogrest.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private ModelMapper mapper;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, ModelMapper mapper){
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.mapper = mapper;
    }



    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {

        Comment comment = mapToEntity(commentDto);

        //retrieve post entity by id
        Post post = postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post", "id", postId));

        //set post to comment entity
        comment.setPost(post);

        //save comment entity
        Comment newComment = commentRepository.save(comment);

        CommentDto addedComment = mapToDto(newComment);

        return addedComment;
    }

    @Override
    public List<CommentDto> getCommentsByPostId(long postId) {

        // retrieve comments by post id
        List<Comment>  comments = commentRepository.findByPostId(postId);


        // convert list of comments to commentDto
        return comments.stream().map(comment -> mapToDto(comment)).collect(Collectors.toList());
    }

    private Post findByPostId(long postId){
        return postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post", "id", postId));
    }

    private Comment findByCommentId(long commentId){
        return commentRepository.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("Comment", "id", commentId));
    }






    @Override
    public CommentDto getCommentById(Long postId, Long commentId) {
/*
        //retrieve post entity by id
        Post post = postRepository.findById(postId).orElseThrow(()->
                new ResourceNotFoundException("Post", "id", postId));

        // retrieve comment by id
        Comment comment = commentRepository.findById(commentId).orElseThrow(()->
                new ResourceNotFoundException("Comment", "id", commentId));

        if (!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment doesn't belongs to post");
        }

        */

        Post post = findByPostId(postId);
        Comment comment = findByCommentId(commentId);

        if (comment.getPost().getId() != post.getId()){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment doesn't belongs to post");
        }

        return mapToDto(comment);
    }

    @Override
    public CommentDto updateComment(Long postId, long commentId, CommentDto commentRequest) {

        Post post = findByPostId(postId);
        Comment comment = findByCommentId(commentId);

        if (comment.getPost().getId() != post.getId()){
            // id'ler uyuşmuyorsa
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment doesn't belongs to post");
        }

        comment.setName(commentRequest.getName());
        comment.setMail(commentRequest.getMail());
        comment.setBody(commentRequest.getBody());

        Comment updatedComment = commentRepository.save(comment);

        return mapToDto(updatedComment);
    }

    @Override
    public void deleteComment(Long postId, long commentId) {

        Post post = findByPostId(postId);
        Comment comment = findByCommentId(commentId);

        if (comment.getPost().getId() != post.getId()){
            // id'ler uyuşmuyorsa
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment doesn't belongs to post");
        }

        commentRepository.delete(comment);

    }

    private CommentDto mapToDto(Comment comment){
        CommentDto commentDtoMap = mapper.map(comment, CommentDto.class);

//        CommentDto commentDto = new CommentDto();
//        commentDto.setId(comment.getId());
//        commentDto.setName(comment.getName());
//        commentDto.setMail(comment.getMail());
//        commentDto.setBody(comment.getBody());


        return commentDtoMap;
    }

    private Comment mapToEntity(CommentDto commentDto){

        Comment commentMap = mapper.map(commentDto, Comment.class);

//        Comment comment = new Comment();
//        comment.setName(commentDto.getName());
//        comment.setMail(commentDto.getMail());
//        comment.setBody(commentDto.getBody());

        return commentMap;

    }

}
