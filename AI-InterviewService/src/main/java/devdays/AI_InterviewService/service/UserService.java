package devdays.AI_InterviewService.service;

import devdays.AI_InterviewService.entity.User;

public interface UserService {
    void signup(User user);
    boolean login(String userId, String password);

}
