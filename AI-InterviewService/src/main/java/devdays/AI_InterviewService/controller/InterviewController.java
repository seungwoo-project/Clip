package devdays.AI_InterviewService.controller;

import devdays.AI_InterviewService.entity.CoverLetter;
import devdays.AI_InterviewService.entity.User;
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
    @Autowired
    public InterviewController(UserService userService, CoverLetterService coverLetterService) {
        this.userService = userService;
        this.coverLetterService = coverLetterService;
    }


    @GetMapping("/")
    public String login_form() {

        return "basic/login";
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
                model.addAttribute("message", "로그인에 실패하였습니다.");
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
    public String register(@ModelAttribute User user) {
        try {
            userService.signup(user);
            return "redirect:/";
        } catch (IllegalArgumentException e) {
            return "basic/register";
        }
    }
    @GetMapping("/list")
    public String list(Model model, HttpSession session) {
        String userId = (String) session.getAttribute("userId");

        if (userId != null) {
            List<CoverLetter> coverLetters = coverLetterService.findByUserId(userId);
            model.addAttribute("coverLetters", coverLetters);
        }

        return "basic/list";
    }
    @PostMapping("/upload")
    public String upload(@RequestParam("coverLetter") MultipartFile file, HttpSession session) throws IOException {
        String userId = (String) session.getAttribute("userId");

        if (userId != null) {
            String title = file.getOriginalFilename();
            String content = new String(file.getBytes(), "UTF-8");
            coverLetterService.saveCoverLetter(userId, title, content);
        }

        return "redirect:/list";
    }

    @GetMapping("/{coverLetterId}")
    public String coverLetterItem(@PathVariable Long coverLetterId, Model model) {
        CoverLetter coverLetterItem = coverLetterService.findByCoverLetterId(coverLetterId);
        model.addAttribute("coverLetter", coverLetterItem);

        return "basic/coverLetter";
    }

//    @GetMapping("/{coverLetter}/delete")
//    public String coverLetterDelete() {
//
//        return "ok";
//    }
//
//    @GetMapping("/{coverLetter}/execute")
//    public String coverLetterExecute() {
//
//        return "ok";
//    }

}
