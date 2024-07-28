package our.devdays.clip.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import our.devdays.clip.entity.CoverLetter;
import our.devdays.clip.repository.CoverLetterRepository;

import java.util.Date;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CoverLetterServiceImpl implements CoverLetterService{

    private final CoverLetterRepository coverLetterRepository;

    @Override
    public void saveCoverLetter(String userId, String title, String content) {
        CoverLetter coverLetter = new CoverLetter();
        coverLetter.setUserId(userId);
        coverLetter.setTitle(title);
        coverLetter.setContent(content);
        coverLetter.setUploadDate(new Date());
        coverLetterRepository.save(coverLetter);
    }

    @Override
    public List<CoverLetter> findByUserId(String userId) {
        return coverLetterRepository.findByUserId(userId);
    }

    @Override
    public CoverLetter findByCoverLetterId(Long coverLetterId) {
        return coverLetterRepository.findByCoverLetterId(coverLetterId);
    }

    @Override
    public void deleteCoverLetter(Long coverLetterId) {
        coverLetterRepository.deleteById(coverLetterId);
    }
}
