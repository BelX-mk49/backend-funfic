package by.itransition.backend.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long roleId;

    private String role;
}
