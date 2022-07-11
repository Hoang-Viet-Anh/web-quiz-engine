package engine.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Email(regexp = ".+@.+\\..+")
    @Column(name = "email", unique = true)
    private String email;

    @Size(min = 5)
    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private String role;
}
