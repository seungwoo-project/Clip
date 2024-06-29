package devdays.AI_InterviewService.controller;

import devdays.AI_InterviewService.controller.validation.UserRegisterForm;
import devdays.AI_InterviewService.entity.User;
import devdays.AI_InterviewService.service.UserService;
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

@Controller
@Slf4j
public class UserController {
    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 로그인 폼
    @GetMapping("/")
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
        return "redirect:/";
    }

    // 로그인 판별 여부에 따라 목록으로 갈건지 다시 로그인폼으로 가는 기능 구현
    @PostMapping("/login")
    public String login(@Validated @ModelAttribute User user, BindingResult bindingResult, Model model, HttpSession session) {

        // 검증 실패
        if(bindingResult.hasErrors()) {
            log.info("검증 오류 발생 = {}", bindingResult);
            return "basic/login";
        }

        if (session.getAttribute("userId") == null) {
            boolean loginSuccess = userService.login(user.getUserId(), user.getPassword());

            if (loginSuccess) {
                session.setAttribute("userId", user.getUserId());
                return "redirect:/list";
            } else {
                model.addAttribute("errorMessage", "아이디 또는 비밀번호를 잘못 입력했습니다.");
                return "basic/login";
            }
        } else {
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
            return "redirect:/";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "basic/register";
        }
    }
}
