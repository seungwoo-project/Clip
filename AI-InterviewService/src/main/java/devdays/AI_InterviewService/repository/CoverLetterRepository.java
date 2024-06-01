package devdays.AI_InterviewService.repository;

import devdays.AI_InterviewService.entity.CoverLetter;
import devdays.AI_InterviewService.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoverLetterRepository extends JpaRepository<CoverLetter, Long> {
}
