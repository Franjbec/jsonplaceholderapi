package com.fake.api.jsonplaceholder.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Comment {
    @Schema(description = "Post identifier", example = "1")
    private Long postId;
    @Schema(description = "Comment identifier", example = "1")
    private Long id;
    @Schema(description = "Comment name", example = "id labore ex et quam laborum")
    private String name;
    @Schema(description = "Comment email", example = "example@email.com")
    private String email;
    @Schema(description = "Comment body", example = "laudantium enim quasi est quidem magnam voluptate ipsam eos\ntempora quo necessitatibus\ndolor quam autem quasi\nreiciendis et nam sapiente accusantium")
    private String body;
}
