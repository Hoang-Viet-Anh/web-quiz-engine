package engine.model.completeTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class CompleteTimeService {
    @Autowired
    private CompleteTimeRepository completeTimeRepository;

    public Page<CompleteTime> getAllPaged(int pageNo, int pageSize, String sortBy, String username) {
        Pageable pageable = PageRequest.of(pageNo, pageSize,
                Sort.by(Sort.Order.desc(sortBy),
                        Sort.Order.desc("id")));

        return completeTimeRepository.findAllByUser(username, pageable);
    }
    public void saveResult(long quizId, String user) {
        completeTimeRepository.save(new CompleteTime(quizId, user));
    }
}
