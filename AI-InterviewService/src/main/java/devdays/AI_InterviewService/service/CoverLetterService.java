package devdays.AI_InterviewService.service;

import devdays.AI_InterviewService.entity.CoverLetter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CoverLetterService {
    List<CoverLetter> findByUserId(String userId);

    void saveCoverLetter(String userId, String title, String content);
}
