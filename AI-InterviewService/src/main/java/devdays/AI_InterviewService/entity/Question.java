package devdays.AI_InterviewService.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity @Getter @Setter
@Table(name = "questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quesId")
    private Long id;

    @Column(name = "userId")
    private String userId;

    @Column(name = "ques")
    private String question;
}
