package devdays.AI_InterviewService.controller;

import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.CompletionResult;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

    // 로그인 폼
    @GetMapping("/")
    public String login_form(HttpSession session) {
        if(session.getAttribute("userId") == null) return "basic/login";
        else return "redirect:/list";
    }

    // 로그아웃시 세션초기화
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("userId");
        session.removeAttribute("coverLetterId");
//        session.setAttribute("userId", null);
//        session.setAttribute("coverLetterId", null);
        return "redirect:/";
    }

    // 로그인 판별 여부에 따라 목록으로 갈건지 다시 로그인폼으로 가는 기능 구현
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

    // 회원가입 폼
    @GetMapping("/register")
    public String register_form() {

        return "basic/register";
    }

    // 회원 데이터베이스 삽입 기능 구현
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

    // 자소서 목록 출력 기능
    @GetMapping("/list")
    public String list(Model model, HttpSession session) {
        if (session.getAttribute("questions") != null) {
            session.removeAttribute("questions");
            session.removeAttribute("coverLetterId");
            session.removeAttribute("selectedQuestions");
            session.removeAttribute("userQuestions");
            session.removeAttribute("gptQuestions");
        }
        String userId = (String) session.getAttribute("userId");

        List<CoverLetter> coverLetters = coverLetterService.findByUserId(userId);
        model.addAttribute("coverLetters", coverLetters);

        return "basic/list";
    }

    // 파일 업로드 기능 구현 후 목록에 출력
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
            session.setAttribute("selectedQuestions", selectedQuestions);
        } else {
            session.setAttribute("selectedQuestions", new Long[0]); // 빈 배열로 설정
        }
        return "basic/addlist";
    }

    // 사용자 질문 선택 후 세션에 저장
    @PostMapping("/list/{coverLetterId}/userSelect")
    public ResponseEntity<Void> addUserQuestions(@RequestBody List<String> userQuestions, HttpSession session) {
        session.setAttribute("userQuestions", userQuestions);
        return ResponseEntity.ok().build();
    }

    // 단순 로딩창 구현
    @GetMapping("/list/{coverLetterId}/loading")
    public String loading() {
        return "basic/loading";
    }

    // 데이터베이스에서 선택질문 + 사용자 질문 추가 후 선택질문 + gpt가 만들어주는 질문 리스트를 종합해서 세션에 담아줌 --> 면접실행
    @PostMapping("/list/{coverLetterId}/loading")
    public String allQuestionsInterview(HttpSession session) {
        Long[] selectedQuestions = (Long[]) session.getAttribute("selectedQuestions");
        List<String> userQuestions = (List<String>) session.getAttribute("userQuestions");
        Long coverLetterId = (Long) session.getAttribute("coverLetterId");

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

<<<<<<< HEAD
//        if (coverLetterId != null) {
//            CoverLetter coverLetter = coverLetterService.findByCoverLetterId(coverLetterId);
//            List<String> gptQuestions = generateQuestionsUsingGPT(coverLetter.getContent());
//            allQuestions.addAll(gptQuestions);
//            log.info("GPT 생성 질문들:");
//            for (String question : gptQuestions) {
//                log.info(question);
//            }
//        } else {
//            log.info("GPT가 질문을 만들지 않았습니다.");
//        }
=======
        if (coverLetterId != null) {
            CoverLetter coverLetter = coverLetterService.findByCoverLetterId(coverLetterId);
            List<String> gptQuestions = generateQuestionsUsingGPT(coverLetter.getContent(), allQuestions);
            session.setAttribute("gptQuestions", gptQuestions);
            allQuestions.addAll(gptQuestions);
            log.info("GPT 생성 질문들:");
            for (String question : gptQuestions) {
                log.info(question);
            }
        } else {
            log.info("GPT가 질문을 만들지 않았습니다.");
        }
>>>>>>> 87ab16eb0148b13e0962041a68242a3d956479cd

        log.info("전체 질문 리스트:");
        for (String question : allQuestions) {
            log.info(question);
        }

        session.setAttribute("questions", allQuestions);

        return "redirect:/list/" + coverLetterId + "/interview";
    }

    // 실제 면접 화면 세션에 저장된 질문들을 꺼내와서 1개씩 보여주는 기능 추가해야함
    @GetMapping("/list/{coverLetterId}/interview")
    public String interviewPage(HttpSession session, Model model) {
        List<String> allQuestions = (List<String>) session.getAttribute("questions");

        if (allQuestions == null || allQuestions.isEmpty()) {
            Long coverLetterId = (Long) session.getAttribute("coverLetterId");
            model.addAttribute("errorMessage", "면접 질문이 준비되지 않았습니다. 질문을 선택하거나 추가해주세요.");
            return "redirect:/list/" + coverLetterId + "/select";
        }
//        model.addAttribute("questions", allQuestions);
        return "basic/interviewmain";
    }

    // 사용자가 추가한 질문리스트와 gpt가 만들어준 질문리스트를 종합해서 모델에 담아준 기능
    @GetMapping("/list/{coverLetterId}/interview/save")
    public String interviewSaveList(HttpSession session, Model model) {
        List<String> userQuestions = (List<String>) session.getAttribute("userQuestions");
        List<String> gptQuestions = (List<String>) session.getAttribute("gptQuestions");

        List<String> saveQuestions = new ArrayList<>();

        if(userQuestions != null) saveQuestions.addAll(userQuestions);
        if(gptQuestions != null) saveQuestions.addAll(gptQuestions);

        model.addAttribute("saveQuestions", saveQuestions);
        return "basic/savelist";
    }


    // 체크박스에 선택된 질문들을 questions 데이터베이스에 삽입하는 기능
    @PostMapping("/list/{coverLetterId}/interview/save")
    public String saveSelectedQuestions(@RequestParam("selectedQuestions") List<String> selectedQuestions, HttpSession session) {
        String userId = (String) session.getAttribute("userId");

        List<Question> questions = selectedQuestions.stream()
                .map(questionText -> new Question(questionText, userId))
                .collect(Collectors.toList());

        questionService.saveAll(questions);

        return "redirect:/list";
    }

    // gpt가 만들어주는 면접질문 리스트
    private List<String> generateQuestionsUsingGPT(String text, List<String> allQuestions) {
        String apiKey = "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";
        OpenAiService service = new OpenAiService(apiKey);

        String content;

        if (allQuestions != null && !allQuestions.isEmpty()) {
            // allQuestions에 질문이 있는 경우
            String questionsText = String.join("\n", allQuestions);
            content = "gpt, you are the developer interview personnel manager from now on. " +
                    "Read the cover letter I show you and make 5 interview questions in Korean except for "+ questionsText + " Cover Letter :\n\n" + text + "\n\nQuestion:\n1.";
        } else {
            // allQuestions에 질문이 없는 경우
            content = "gpt, you are the developer interview personnel manager from now on. " +
                    "Read the cover letter I show you and make 5 interview questions in Korean:\n\n" +
                    "Cover Letter:\n" + text + "\n\nQuestion:\n1.";
        }


        log.info("프롬프트 내용 : {}" , content);
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .messages(Arrays.asList(new ChatMessage("system", content)))
                .model("gpt-3.5-turbo")
                .maxTokens(300)
                .n(1)
                .build();

        List<String> questions = new ArrayList<>();

        try {
            ChatCompletionResult chatCompletionResult = service.createChatCompletion(chatCompletionRequest);
            String generatedText = chatCompletionResult.getChoices().get(0).getMessage().getContent().trim();
            String[] lines = generatedText.split("\n");

            for (String line : lines) {
                line = line.trim();
                if (line.matches("^\\d+\\..*")) {
                    questions.add(line.replaceFirst("^\\d+\\.", "").trim());
                }
            }

            // 요청 간격 조절
            Thread.sleep(1000);
        } catch (Exception e) {
            // Handle exceptions
            e.printStackTrace();
        }

        return questions;
    }

}
