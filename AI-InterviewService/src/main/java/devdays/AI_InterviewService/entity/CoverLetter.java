package devdays.AI_InterviewService.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "coverletters")
public class CoverLetter {
    @Id
    @Column(name = "coverLetterId")
    private String coverLetterId;

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "uploadDate")
    @Temporal(TemporalType.DATE)
    private Date uploadDate;

    @Column(name = "content", nullable = false)
    private String content;

    // Getters and Setters

    // 생성자, equals, hashCode 등 필요한 메서드
}