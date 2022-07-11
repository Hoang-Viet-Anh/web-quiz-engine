package engine.model.quiz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuizService {
    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuizPageRepository quizPageRepository;

    public Quiz addQuiz(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    public boolean existById(long id) {
        return quizRepository.existsById(id);
    }

    public Quiz getById(long id) {
        return quizRepository.findById(id).orElse(null);
    }

    public List<Quiz> getAll() {
        List<Quiz> quizList = new ArrayList<>();
        quizRepository.findAll().forEach(quizList::add);
        return quizList;
    }

    public void deleteQuiz(Quiz quiz) {
        quizRepository.delete(quiz);
    }

    public Page<Quiz> getAllPaged(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        return quizPageRepository.findAll(pageable);
    }
}
