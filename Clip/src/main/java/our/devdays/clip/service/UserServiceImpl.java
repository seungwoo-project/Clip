package our.devdays.clip.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import our.devdays.clip.entity.User;
import our.devdays.clip.entity.Question;
import our.devdays.clip.repository.QuestionRepository;
import our.devdays.clip.repository.UserRepository;

import java.util.Arrays;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;

    public void signup(User user) {
        // 중복된 사용자 아이디 체크
        if (userRepository.findByUserId(user.getUserId()) != null) {
            throw new IllegalArgumentException("중복 아이디입니다. 다른 아이디를 입력해 주세요.");
        }
        userRepository.save(user);
        List<Question> defaultQuestions = Arrays.asList(
                new Question("간단하게 자기소개와 지원동기 말씀해주세요", user.getUserId()),
                new Question("자신의 장점과 단점에 대해 말해주세요", user.getUserId()),
                new Question("입사 후 포부 및 비전에 대해 말해주세요", user.getUserId())
        );
        questionRepository.saveAll(defaultQuestions);
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
