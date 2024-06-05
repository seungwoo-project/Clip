package devdays.AI_InterviewService.service;

import devdays.AI_InterviewService.entity.Question;
import devdays.AI_InterviewService.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;

    public QuestionServiceImpl(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Override
    public List<Question> getAllQuestionsByUserId(String userId) {
        return questionRepository.findByUserId(userId);
    }

    @Override
    public List<Question> getQuestionsByIds(Long[] questionIds) {
        return questionRepository.findAllById(Arrays.asList(questionIds));
    }

    @Override
    public void saveAll(List<Question> selectedQuestions) {
        questionRepository.saveAll(selectedQuestions);
    }

}