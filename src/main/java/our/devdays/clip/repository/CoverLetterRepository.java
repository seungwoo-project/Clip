package our.devdays.clip.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import our.devdays.clip.entity.CoverLetter;

import java.util.List;

@Repository
public interface CoverLetterRepository extends JpaRepository<CoverLetter, Long> {
    List<CoverLetter> findByUserId(String userId);
    CoverLetter findByCoverLetterId(Long coverLetterId);
}
