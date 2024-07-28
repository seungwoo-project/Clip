package our.devdays.clip.service;


import our.devdays.clip.entity.Question;

import java.util.List;

public interface QuestionService {
    List<Question> getAllQuestionsByUserId(String userId);

    List<Question> getQuestionsByIds(Long[] questionIds);

    void saveAll(List<Question> selectedQuestions);

    void deleteQuestion(Long questionId);
}
