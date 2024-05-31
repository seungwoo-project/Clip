package devdays.AI_InterviewService.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
//@ResponseBody
public class InterviewController {

    @GetMapping("/")
    public String 로그인폼() {

        return "ok";
    }
}
