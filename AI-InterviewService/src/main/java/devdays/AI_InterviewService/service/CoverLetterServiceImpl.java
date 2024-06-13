package devdays.AI_InterviewService.service;

import devdays.AI_InterviewService.entity.CoverLetter;
import devdays.AI_InterviewService.entity.User;
import devdays.AI_InterviewService.repository.CoverLetterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;


@Service
public class CoverLetterServiceImpl implements CoverLetterService{
    private final CoverLetterRepository coverLetterRepository;

    @Autowired
    public CoverLetterServiceImpl(CoverLetterRepository coverLetterRepository) {
        this.coverLetterRepository = coverLetterRepository;
    }

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
