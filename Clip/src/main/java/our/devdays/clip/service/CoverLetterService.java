package our.devdays.clip.service;

import our.devdays.clip.entity.CoverLetter;
import java.util.List;

public interface CoverLetterService {
    List<CoverLetter> findByUserId(String userId);

    void saveCoverLetter(String userId, String title, String content);

    CoverLetter findByCoverLetterId(Long coverLetterId);
    void deleteCoverLetter(Long coverLetterId);
}
