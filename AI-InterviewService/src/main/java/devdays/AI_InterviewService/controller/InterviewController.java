package devdays.AI_InterviewService.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
//@ResponseBody
public class InterviewController {

    @GetMapping("/")
    public String login_form() {

        return "basic/login";
    }

    @PostMapping("/list")
    public String login() {

        return "basic/list";
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
    public String register() {

        return "redirect:/";
    }

    @PostMapping("/upload")
    public String upload() {

        return "redirect:/list";
    }
//
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
