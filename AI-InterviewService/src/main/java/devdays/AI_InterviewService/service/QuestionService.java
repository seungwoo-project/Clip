package devdays.AI_InterviewService.service;

import devdays.AI_InterviewService.entity.Question;

import java.util.List;

public interface QuestionService {
    List<Question> getAllQuestionsByUserId(String userId);
}
