package com.example.springbootblogrest.payload;

import com.example.springbootblogrest.entity.Comment;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class PostDto {

    private long id;

    // title should not be empty
    @NotEmpty
    @Size(min = 2, message = "Should be more than 2")
    private String title;


    @NotEmpty
    @Size(min = 2, message = "Should be more than 2")
    private String description;

    @NotEmpty
    private String content;

    private Set<CommentDto> comments;

    private Long categoryId;

}
