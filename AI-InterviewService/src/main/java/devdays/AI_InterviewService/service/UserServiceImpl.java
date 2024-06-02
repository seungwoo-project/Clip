package devdays.AI_InterviewService.service;

import devdays.AI_InterviewService.entity.User;
import devdays.AI_InterviewService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void signup(User user) {
        // 아이디 null 또는 빈 문자열 체크
        if (user.getUserId() == null || user.getUserId().isEmpty()) {
            throw new IllegalArgumentException("아이디를 입력해 주세요.");
        }
        // 중복된 사용자 아이디 체크
        if (userRepository.findByUserId(user.getUserId()) != null) {
            throw new IllegalArgumentException("사용할 수 없는 아이디입니다. 다른 아이디를 입력해 주세요.");
        }
        // 비밀번호 null 또는 빈 문자열 체크
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new IllegalArgumentException("비밀번호를 입력해 주세요.");
        }
        userRepository.save(user);
    }

    public boolean login(String userId, String password) {
        User user = userRepository.findByUserId(userId);

        if (user != null && user.getPassword().equals(password)) {
            // 로그인 성공
            return true;
        } else {
            // 로그인 실패
            return false;
        }
    }
}
