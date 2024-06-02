package devdays.AI_InterviewService.controller;

import devdays.AI_InterviewService.entity.CoverLetter;
import devdays.AI_InterviewService.entity.User;
import devdays.AI_InterviewService.repository.CoverLetterRepository;
import devdays.AI_InterviewService.service.CoverLetterService;
import devdays.AI_InterviewService.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
//@ResponseBody
public class InterviewController {

    private final UserService userService;
    private final CoverLetterService coverLetterService;
    private final CoverLetterRepository coverLetterRepository;
    @Autowired
    public InterviewController(UserService userService, CoverLetterService coverLetterService, CoverLetterRepository coverLetterRepository) {
        this.userService = userService;
        this.coverLetterService = coverLetterService;
        this.coverLetterRepository = coverLetterRepository;
    }


    @GetMapping("/")
    public String login_form(HttpSession session) {
        if(session.getAttribute("userId") == null) return "basic/login";
        else return "redirect:/list";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.setAttribute("userId", null);
        return "redirect:/";
    }

    @PostMapping("/list")
    public String login(@RequestParam String userId, @RequestParam String password, Model model, HttpSession session) {
        if(session.getAttribute("userId") == null) {
            boolean loginSuccess = userService.login(userId, password);

            if (loginSuccess) {
                model.addAttribute("message", "로그인이 성공하였습니다.");
                session.setAttribute("userId", userId); // 세션에 사용자 아이디 저장
                return "redirect:/list";
            } else {
                model.addAttribute("errorMessage", "아이디 또는 비밀번호를 잘못 입력했습니다.");
                return "basic/login";
            }
        } else {
            return "redirect:/list";
        }

    }

    @GetMapping("/register")
    public String register_form() {

        return "basic/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user, Model model) {
        try {
            userService.signup(user);
            return "redirect:/";
        } catch (IllegalArgumentException e) {
            model.addAttribute("registerErrorMessage", "사용할 수 없는 아이디입니다. 다른 아이디를 입력해 주세요.");
            return "basic/register";
        }
    }
    @GetMapping("/list")
    public String list(Model model, HttpSession session) {
        String userId = (String) session.getAttribute("userId");

        List<CoverLetter> coverLetters = coverLetterService.findByUserId(userId);
        model.addAttribute("coverLetters", coverLetters);

        return "basic/list";
    }
    @PostMapping("/upload")
    public String upload(@RequestParam("coverLetter") MultipartFile file, HttpSession session) throws IOException {
        String userId = (String) session.getAttribute("userId");

        if (userId != null) {
            String originalFilename = file.getOriginalFilename();
            String title = originalFilename.substring(0, originalFilename.lastIndexOf('.'));
            String content = new String(file.getBytes(), "UTF-8");
            coverLetterService.saveCoverLetter(userId, title, content);
        }

        return "redirect:/list";
    }

    //상세조회
    @GetMapping("/list/{coverLetterId}")
    public String coverLetterItem(@PathVariable Long coverLetterId, Model model) {
        CoverLetter coverLetterItem = coverLetterService.findByCoverLetterId(coverLetterId);
        model.addAttribute("coverLetter", coverLetterItem);

        return "basic/coverLetter";
    }

    //삭제
    @PostMapping("/list/{coverLetterId}/delete")
    public String coverLetterDelete(@PathVariable Long coverLetterId) {
        coverLetterService.deleteCoverLetter(coverLetterId);
        return "redirect:/list";
    }

    //면접실행
//    @GetMapping("/{coverLetter}/execute")
//    public String coverLetterExecute() {
//
//        return "ok";
//    }

}
