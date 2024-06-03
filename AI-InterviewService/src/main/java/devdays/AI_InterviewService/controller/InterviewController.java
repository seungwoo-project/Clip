package devdays.AI_InterviewService.controller;

import devdays.AI_InterviewService.entity.CoverLetter;
import devdays.AI_InterviewService.entity.Question;
import devdays.AI_InterviewService.entity.User;
import devdays.AI_InterviewService.repository.CoverLetterRepository;
import devdays.AI_InterviewService.service.CoverLetterService;
import devdays.AI_InterviewService.service.QuestionService;
import devdays.AI_InterviewService.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Slf4j
public class InterviewController {

    private final UserService userService;
    private final CoverLetterService coverLetterService;
    private final QuestionService questionService;
    @Autowired
    public InterviewController(UserService userService, CoverLetterService coverLetterService, CoverLetterRepository coverLetterRepository, QuestionService questionService) {
        this.userService = userService;
        this.coverLetterService = coverLetterService;
        this.questionService = questionService;
    }


    @GetMapping("/")
    public String login_form(HttpSession session) {
        if(session.getAttribute("userId") == null) return "basic/login";
        else return "redirect:/list";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.setAttribute("userId", null);
        session.setAttribute("coverLetterId", null);
        return "redirect:/";
    }

    @PostMapping("/list")
    public String login(@RequestParam String userId, @RequestParam String password, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("userId") == null) {
            boolean loginSuccess = userService.login(userId, password);

            if (loginSuccess) {
                redirectAttributes.addFlashAttribute("message", "로그인이 성공하였습니다.");
                session.setAttribute("userId", userId);
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
            model.addAttribute("registerErrorMessage", e.getMessage());
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

    //질문불러오기 페이지
    @GetMapping("/list/{coverLetterId}/select")
    public String coverLetterSelect(@PathVariable Long coverLetterId, HttpSession session, Model model) {
        session.setAttribute("coverLetterId", coverLetterId);
        String userId = (String) session.getAttribute("userId");

        //데이터베이스 있을 때
        List<Question> questions = questionService.getAllQuestionsByUserId(userId);
        model.addAttribute("questions", questions);

        return "basic/selectlist";
    }

    // 질문선택 후 세션에 저장
    @PostMapping("/list/{coverLetterId}/select")
    public String addSelectedQuestions(@RequestParam(value = "selectedQuestions", required = false) Long[] selectedQuestions, HttpSession session) {
        if (selectedQuestions != null) {
            // 선택된 질문 식별자 로그 출력
            for (Long questionId : selectedQuestions) {
                log.info("선택된 질문 식별자: {}", questionId);
            }
            session.setAttribute("selectedQuestions", selectedQuestions);
        } else {
            log.info("선택된 질문이 없습니다.");
            session.setAttribute("selectedQuestions", new Long[0]); // 빈 배열로 설정
        }
        return "basic/addlist";
    }

    // 사용자 질문 선택 후 세션에 저장
    @PostMapping("/list/{coverLetterId}/userSelect")
    public String addUserQuestions(@RequestBody List<String> userQuestions, HttpSession session) {
        session.setAttribute("userQuestions", userQuestions);
        return "redirect:/list/{coverLetterId}/loading";
    }

    @GetMapping("/list/{coverLetterId}/loading")
    public String loading(HttpSession session, Model model) {

        return "basic/loading";
    }

    @GetMapping("/list/{coverLetterId}/interview")
    public String interviewPage(HttpSession session, Model model) {
        Long[] selectedQuestions = (Long[]) session.getAttribute("selectedQuestions");
        List<String> userQuestions = (List<String>) session.getAttribute("userQuestions");

        List<String> allQuestions = new ArrayList<>();

        if (selectedQuestions != null) {
            List<Question> selectedQuestionList = questionService.getQuestionsByIds(selectedQuestions);
            for (Question question : selectedQuestionList) {
                allQuestions.add(question.getQuestion());
            }
            log.info("선택된 질문들:");
            for (String question : allQuestions) {
                log.info(question);
            }
        } else {
            log.info("선택된 질문이 없습니다.");
        }

        if (userQuestions != null) {
            allQuestions.addAll(userQuestions);
            log.info("사용자 추가 질문들:");
            for (String question : userQuestions) {
                log.info(question);
            }
        } else {
            log.info("사용자 추가 질문이 없습니다.");
        }

        log.info("전체 질문 리스트:");
        for (String question : allQuestions) {
            log.info(question);
        }

        model.addAttribute("questions", allQuestions);
        return "basic/interviewmain";
    }

}
