package devdays.AI_InterviewService.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
//@ResponseBody
public class LoginController {

    @GetMapping("/")
    public String 로그인폼() {

        return "basic/login";
    }

    @PostMapping("/")
    public String login() {

        return "basic/list";
    }

    @GetMapping("/register")
    public String register() {

        return "basic/register";
    }
}
