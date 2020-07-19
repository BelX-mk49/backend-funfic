package by.itransition.backend.service;

import by.itransition.backend.exceptions.GenreNotFoundException;
import by.itransition.backend.exceptions.PostNotFoundException;
import by.itransition.backend.mapper.PostMapper;
import by.itransition.backend.model.Genre;
import by.itransition.backend.model.Post;
import by.itransition.backend.model.User;
import by.itransition.backend.payload.request.PostRequest;
import by.itransition.backend.payload.resposne.PostResponse;
import by.itransition.backend.repo.GenreRepository;
import by.itransition.backend.repo.PostRepository;
import by.itransition.backend.repo.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final GenreRepository genreRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final PostMapper postMapper;

    public void save(PostRequest postRequest) {
        Genre genre = genreRepository.findByName(postRequest.getGenreName())
                .orElseThrow(() -> new GenreNotFoundException(postRequest.getGenreName()));
        postRepository.save(postMapper.map(postRequest, genre, authService.getCurrentUser()));
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id.toString()));
        return postMapper.mapToDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(postMapper::mapToDto)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByGenre(Long genreId) {
        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new GenreNotFoundException(genreId.toString()));
        List<Post> posts = postRepository.findAllByGenre(genre);
        return posts.stream().map(postMapper::mapToDto).collect(toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return postRepository.findByUser(user)
                .stream()
                .map(postMapper::mapToDto)
                .collect(toList());
    }
}
