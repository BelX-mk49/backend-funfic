package by.itransition.backend.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@EqualsAndHashCode
@NoArgsConstructor
public class Chapter {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String chapterName;

    private String content;

    @OneToOne
    @JoinColumn
    private Image image;

    @JoinColumn
    @ManyToOne(fetch = EAGER)
    private Post post;
}
