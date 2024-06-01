package devdays.AI_InterviewService.controller;

import devdays.AI_InterviewService.entity.User;
import devdays.AI_InterviewService.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
//@ResponseBody
public class InterviewController {

    private final UserService userService;

    @Autowired
    public InterviewController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/")
    public String login_form() {

        return "basic/login";
    }

    @PostMapping("/list")
    public String login(@RequestParam String userId, @RequestParam String password) {
        boolean loginSuccess = userService.login(userId, password);

        if (loginSuccess) {
            return "basic/list";
        } else {
            return "basic/login";
        }
    }

    @GetMapping("/list")
    public String listAll() {
//        List<File> files = fileRepository.findByUserId(userId);
//        model.addAttribute("files", files);
        return "basic/list";
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

    @PostMapping("/upload")
    public String upload() {

        return "redirect:/list";
    }

//    @GetMapping("/{coverLetter}")
//    public String coverLetter() {
//
//        return "ok";
//    }
//
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
