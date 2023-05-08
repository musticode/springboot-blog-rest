package com.example.springbootblogrest.controller;

import com.example.springbootblogrest.entity.Comment;
import com.example.springbootblogrest.payload.CommentDto;
import com.example.springbootblogrest.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {

    private CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService){
        this.commentService = commentService;
    }

    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable(name = "postId") Long postId,
                                @PathVariable(name = "commentId") Long commentId){

        commentService.deleteComment(postId, commentId);
        return new ResponseEntity<>("Comment deleted successfully", HttpStatus.OK);
    }



    // api/posts/{postId}/comments/{commentId} --> update comment by id if it belongs to post with id = postId
    @PutMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable(name = "postId") Long postId,
                                                    @PathVariable(name = "commentId") Long commentId,
                                                    @RequestBody CommentDto commentDto){

        CommentDto updatedComment = commentService.updateComment(postId, commentId, commentDto);


        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }




    // api/posts/{postId}/comments/{id} -> get comment by id if it belongs to post with id = postId
    @GetMapping("/post/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDto> getComment(@PathVariable(name = "postId")Long postId,
                                 @PathVariable(name = "commentId")Long commentId){

        CommentDto commentDto = commentService.getCommentById(postId, commentId);


        return new ResponseEntity<>(commentDto, HttpStatus.OK);

    }




    @GetMapping("/posts/{postId}/comments")
    public List<CommentDto> getCommentsByPostId(@PathVariable(name = "postId")Long postId){

        return commentService.getCommentsByPostId(postId);
    }


    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentDto> createComment(@PathVariable(name = "postId") long postId,
                                                    @RequestBody CommentDto commentDto){

        return new ResponseEntity<>(commentService.createComment(postId, commentDto), HttpStatus.CREATED);

    }



}
