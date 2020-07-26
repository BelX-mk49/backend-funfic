package by.itransition.backend.service;

import by.itransition.backend.payload.request.CommentsRequest;
import by.itransition.backend.payload.resposne.CommentsResponse;
import by.itransition.backend.exceptions.PostNotFoundException;
import by.itransition.backend.mapper.CommentMapper;
import by.itransition.backend.model.Comment;
import by.itransition.backend.model.Post;
import by.itransition.backend.model.User;
import by.itransition.backend.repository.CommentRepository;
import by.itransition.backend.repository.PostRepository;
import by.itransition.backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class CommentService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final UserServiceImpl userService;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;

    public void save(CommentsRequest commentsRequest) {
        Post post = postRepository.findById(commentsRequest.getPostId())
                .orElseThrow(() -> new PostNotFoundException(commentsRequest.getPostId().toString()));
        User currentUser = commentsRequest.getUser();
        Comment comment = commentMapper.map(commentsRequest, post, currentUser);
        commentRepository.save(comment);
    }

    public List<CommentsResponse> getAllCommentsForPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId.toString()));
        return commentRepository.findByPost(post)
                .stream()
                .map(commentMapper::mapToDto).collect(toList());
    }

    public List<CommentsResponse> getAllCommentsForUser(String userName) {
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException(userName));
        return commentRepository.findAllByUser(user)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(toList());
    }
}
