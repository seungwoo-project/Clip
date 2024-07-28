package our.devdays.clip.controller;


import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import our.devdays.clip.entity.CoverLetter;
import our.devdays.clip.entity.Question;
import our.devdays.clip.service.CoverLetterService;
import our.devdays.clip.service.QuestionService;

import java.io.IOException;
import java.util.List;

@Controller
@Slf4j
public class InterviewReadyController {
    private final CoverLetterService coverLetterService;
    private final QuestionService questionService;
    @Autowired
    public InterviewReadyController(CoverLetterService coverLetterService, QuestionService questionService) {
        this.coverLetterService = coverLetterService;
        this.questionService = questionService;
    }

    // 자소서 목록 출력 기능
    @GetMapping("/list")
    public String list(Model model, HttpSession session) {
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

    // 질문불러오기 목록 페이지에서 삭제버튼 클릭시 questions데이터베이스에 참조하여 삭제하는 기능
    @GetMapping("/list/{questionId}/select/delete")
    public String selectListDelete(@PathVariable Long questionId, HttpSession session) {
        questionService.deleteQuestion(questionId);
        Long coverLetterId = (Long)session.getAttribute("coverLetterId");
        return "redirect:/list/" + coverLetterId + "/select";
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
}
