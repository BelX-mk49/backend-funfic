package by.itransition.backend.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import java.awt.*;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public class User {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long userId;

    @NotBlank(message = "Username can't be empty")
    private String username;
    @NotBlank(message = "Password can't be empty")
    private String password;
    private boolean active;

    @Email(message = "Email isn't correct")
    @NotBlank(message = "Email can't be empty")
    private String email;
    private String activationCode;

    @OneToOne
    @JoinColumn(name = "image_id")
    private Image avatar;

}
