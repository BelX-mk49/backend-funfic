package by.itransition.backend.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;

import java.util.HashSet;
import java.util.Set;

import static javax.persistence.CascadeType.REMOVE;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.IDENTITY;
import static org.hibernate.annotations.FetchMode.SELECT;

@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode
public class Genre {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String genreName;

    @Fetch(value = SELECT)
    @ManyToMany(fetch = EAGER, cascade = REMOVE)
    @JoinTable(name = "genre_post",
            joinColumns = {@JoinColumn(name = "genre_id")},
            inverseJoinColumns = {@JoinColumn (name = "post_id")})
    private Set<Post> posts = new HashSet<>();
}
