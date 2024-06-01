package devdays.AI_InterviewService.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity @Getter @Setter
@Table(name = "coverletters")
public class CoverLetter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long coverLetterId;

    @Column(name = "userId")
    private String userId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "uploadDate")
    @Temporal(TemporalType.DATE)
    private Date uploadDate;

    @Column(name = "content", nullable = false)
    private String content;

}