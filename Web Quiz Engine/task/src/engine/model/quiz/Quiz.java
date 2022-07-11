package engine.model.quiz;

import engine.model.user.User;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    @Column(name = "title")
    private String title;

    @NotBlank
    @Column(name = "text")
    private String text;

    @NotNull
    @Size(min = 2, max = 4)
    @Column(name = "options")
    @ElementCollection
    private List<String> options;

    @Size(max = 4)
    @ElementCollection
    private List<Integer> answer;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
}
