package our.devdays.clip.controller;


import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import our.devdays.clip.controller.validation.UserRegisterForm;
import our.devdays.clip.entity.User;
import our.devdays.clip.service.UserService;

@Controller
@Slf4j
public class UserController {
    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 로그인 폼
    @GetMapping("/login")
    public String login_form(HttpSession session, Model model) {
        if(session.getAttribute("userId") == null) {
            model.addAttribute("user", new User());
            return "basic/login";
        }
        else return "redirect:/list";
    }

    // 로그아웃시 세션초기화
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션 무효화
        return "redirect:/login";
    }

    // 로그인 판별 여부에 따라 목록으로 갈건지 다시 로그인폼으로 가는 기능 구현
    @PostMapping("/login")
    public String login(@ModelAttribute User user, Model model, HttpSession session) {

        // 세션아이디가 설정이 안되어있을 때
        if (session.getAttribute("userId") == null) {
            boolean loginSuccess = userService.login(user.getUserId(), user.getPassword());

            // 로그인 실패 처리
            if(!loginSuccess) {
                model.addAttribute("errorMessage", "아이디 또는 비밀번호를 잘못 입력했습니다.");
                return "basic/login";
            }

            // 로그인 성공 처리
            session.setAttribute("userId", user.getUserId());
            return "redirect:/list";

        } else { // 세션아이디가 설정 되어 있을 때

            // 로그인 아이디와 세션아이디와 비교
            log.info("현재 session ID : {}", session.getAttribute("userId"));
            // 세션 아이디와 로그인 한 아이디가 다르다면 갱신해주기
            if(!user.getUserId().equals(session.getAttribute("userId"))) {
                session.setAttribute("userId", user.getUserId());
            }
            return "redirect:/list";
        }
    }

    // 회원가입 폼
    @GetMapping("/register")
    public String register_form(Model model) {
        model.addAttribute("user", new UserRegisterForm());
        return "basic/register";
    }

    // 회원 데이터베이스 삽입 기능 구현
    @PostMapping("/register")
    public String register(@Validated @ModelAttribute("user") UserRegisterForm form, BindingResult bindingResult, Model model) {

        // 비밀번호 일치 여부 검증
        if (!form.getPassword().equals(form.getPasswordConfirm())) {
            bindingResult.reject("passwordWrong");
        }

        // 검증 실패
        if(bindingResult.hasErrors()) {
            log.info("검증 오류 발생 = {}", bindingResult);
            return "basic/register";
        }

        // 검증 성공
        User user = new User();
        user.setUserId(form.getUserId());
        user.setPassword(form.getPassword());

        try {
            userService.signup(user);
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "basic/register";
        }
    }
}
