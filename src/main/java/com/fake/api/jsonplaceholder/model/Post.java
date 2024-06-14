package com.fake.api.jsonplaceholder.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Post {
    @Schema(description = "Post identifier", example = "1")
    private Long id;
    @Schema(description = "User identifier", example = "1")
    private Long userId;
    @Schema(description = "Post title", example = "sunt aut facere repellat provident occaecati excepturi optio reprehenderit")
    private String title;
    @Schema(description = "Post body", example = "quia et suscipit\nsuscipit recusandae consequuntur expedita et cum\nreprehenderit molestiae ut ut quas totam\nnostrum rerum est autem sunt rem eveniet architecto")
    private String body;
}
