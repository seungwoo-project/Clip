package devdays.AI_InterviewService.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
//@ResponseBody
public class LoginController {

    @GetMapping("/")
    public String login_form() {

        return "basic/login";
    }

    @PostMapping("/list")
    public String login() {
//        List<File> files = fileRepository.findByUserId(userId);
//        model.addAttribute("files", files);
        return "basic/list";
    }

    @GetMapping("/register")
    public String register_form() {

        return "basic/register";
    }

    @PostMapping("/register")
    public String register() {

        return "redirect:/";
    }

    @PostMapping("/upload")
    public String upload() {

        return "ok";
    }
}
