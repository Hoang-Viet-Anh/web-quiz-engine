package engine.model.completeTime;

import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "timeOfComplete")
public class CompleteTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long completeId;

    @Expose
    @Column(name = "quizId")
    private long id;

    @Column(name = "time")
    private LocalDateTime completedAt;

    @Column(name = "user")
    private String user;

    public CompleteTime(long id, String user) {
        this.id = id;
        this.user = user;
        completedAt = LocalDateTime.now();
    }
}
