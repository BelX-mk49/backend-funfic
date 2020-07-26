package by.itransition.backend.payload.request;

import by.itransition.backend.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentsRequest {
    private Long id;
    private Long postId;
    private Instant createdDate;
    private String text;
    private String userName;
    private User user;
}
