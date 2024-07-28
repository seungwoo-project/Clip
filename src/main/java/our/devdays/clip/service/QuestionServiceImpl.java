package our.devdays.clip.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import our.devdays.clip.entity.Question;
import our.devdays.clip.repository.QuestionRepository;

import java.util.Arrays;
import java.util.List;


@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;

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

    @Override
    public void deleteQuestion(Long questionId) {
        questionRepository.deleteById(questionId);
    }

}