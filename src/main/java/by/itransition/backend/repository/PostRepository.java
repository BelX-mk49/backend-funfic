package by.itransition.backend.repository;

import by.itransition.backend.model.Genre;
import by.itransition.backend.model.Post;
import by.itransition.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByGenre(Genre genre);

    List<Post> findByUser(User user);
}
