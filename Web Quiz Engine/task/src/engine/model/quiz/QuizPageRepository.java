package engine.model.quiz;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizPageRepository extends PagingAndSortingRepository<Quiz, Long> {

}
