package our.devdays.clip.controller;


import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import our.devdays.clip.entity.CoverLetter;
import our.devdays.clip.entity.Question;
import our.devdays.clip.service.CoverLetterService;
import our.devdays.clip.service.QuestionService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Slf4j
public class InterviewController {

    private final CoverLetterService coverLetterService;
    private final QuestionService questionService;
    @Autowired
    public InterviewController(CoverLetterService coverLetterService, QuestionService questionService) {
        this.coverLetterService = coverLetterService;
        this.questionService = questionService;
    }

    // 단순 로딩창 구현
    @GetMapping("/list/{coverLetterId}/loading")
    public String loading() {
        return "basic/loading";
    }

    // 데이터베이스에서 선택질문 + 사용자 질문 추가 후 선택질문 + gpt가 만들어주는 질문 리스트를 종합해서 세션에 담아줌 --> 면접실행
    @PostMapping("/list/{coverLetterId}/loading")
    public String allQuestionsInterview(@RequestParam("questionCount") int questionCount,HttpSession session) {
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


        if (questionCount > 0) {
            if (coverLetterId != null) {
                CoverLetter coverLetter = coverLetterService.findByCoverLetterId(coverLetterId);
                List<String> gptQuestions = generateQuestionsUsingGPT(coverLetter.getContent(), allQuestions, questionCount);
                allQuestions.addAll(gptQuestions);
                session.setAttribute("gptQuestions", gptQuestions);
                log.info("GPT 생성 질문들:");
                for (String question : gptQuestions) {
                    log.info(question);
                }
            }
        } else {
            log.info("GPT가 질문을 만들지 않았습니다.");
        }

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

        return "basic/interviewmain";
    }

    // 면접이 끝났습니다.
    @GetMapping("/list/{coverLetterId}/interview/finish")
    public String interviewfinish() {

        return "basic/interviewfinish";
    }

    // 사용자가 추가한 질문리스트와 gpt가 만들어준 질문리스트를 종합해서 모델에 담아준 기능
    @GetMapping("/list/{coverLetterId}/interview/save")
    public String interviewSaveList(HttpSession session, Model model) {
        List<String> userQuestions = (List<String>) session.getAttribute("userQuestions");
        List<String> gptQuestions = (List<String>) session.getAttribute("gptQuestions");

        List<String> saveQuestions = new ArrayList<>();

        if(userQuestions != null) saveQuestions.addAll(userQuestions);
        if(gptQuestions != null) saveQuestions.addAll(gptQuestions);
        log.info("저장 되는 질문 리스트:");
        for (String question : saveQuestions) {
            log.info(question);
        }
        model.addAttribute("saveQuestions", saveQuestions);
        return "basic/savelist";
    }


    // 체크박스에 선택된 질문들을 questions 데이터베이스에 삽입하는 기능
    @PostMapping("/list/{coverLetterId}/interview/save")
    public String saveSelectedQuestions(@RequestParam(value = "selectedQuestions", required = false) List<String> selectedQuestions, HttpSession session) {

        if(selectedQuestions != null && !selectedQuestions.isEmpty()) {
            String userId = (String) session.getAttribute("userId");

            List<Question> questions = selectedQuestions.stream()
                    .map(questionText -> new Question(questionText, userId))
                    .collect(Collectors.toList());

            questionService.saveAll(questions);
        }

        return "redirect:/list";
    }



    // gpt가 만들어주는 면접질문 리스트

    @Value("${openai.api.key}")
    private String apiKey;

    private List<String> generateQuestionsUsingGPT(String text, List<String> allQuestions, int questionCount) {


        OpenAiService service = new OpenAiService(apiKey);


        String content;
        String questionsText = String.join("\n", allQuestions);

        if (allQuestions != null && !allQuestions.isEmpty()) {
            // allQuestions에 질문이 있는 경우
            content = "gpt, 지금부터 당신은 개발자 면접관 역할을 합니다. " +
                    "제가 제공하는 자기소개서를 읽고, 기존 질문들을 제외한 나머지 내용에 기반하여 새로운 면접 질문 " + questionCount + "개를 Q1. 이런 식으로 한국어로 작성해 주세요.\n\n" +
                    "기존 질문들:\n" + questionsText + "\n\n" +
                    "자기소개서 내용:\n" + text + "\n\n";
        } else {
            // allQuestions에 질문이 없는 경우
            content = "gpt, 지금부터 당신은 개발자 면접관 역할을 합니다. " +
                    "제가 제공하는 자기소개서를 읽고, 내용에 기반하여 면접 질문 " + questionCount + "개를 Q1. 이런 식으로 한국어로 작성해 주세요.\n\n" +
                    "자기소개서 내용:\n" + text + "\n\n";
        }

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .messages(Arrays.asList(new ChatMessage("system", content)))
                .model("gpt-3.5-turbo")
                .maxTokens(500)
                .n(1)
                .build();

        List<String> questions = new ArrayList<>();
        try {
            ChatCompletionResult chatCompletionResult = service.createChatCompletion(chatCompletionRequest);
            String generatedText = chatCompletionResult.getChoices().get(0).getMessage().getContent().trim();
            String[] lines = generatedText.split("\n");

            for (String line : lines) {
                line = line.trim();
                if (!line.isEmpty() && line.matches("^Q\\d+\\.\\s*(.*)")) {
                    questions.add(line.replaceAll("^Q\\d+\\.\\s*", "").trim());
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
