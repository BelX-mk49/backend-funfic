package by.itransition.backend.payload.request;

import by.itransition.backend.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {
    private Long postId;
    private String genreName;
    private String postName;
    private String url;
    private String description;
    private User user;
}
