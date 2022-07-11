package engine.model.completeTime;

import org.springframework.data.domain.Page;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;


@Repository
public interface CompleteTimeRepository extends PagingAndSortingRepository<CompleteTime, Long> {
    Page<CompleteTime> findAllByUser(String user, Pageable pageable);
}
