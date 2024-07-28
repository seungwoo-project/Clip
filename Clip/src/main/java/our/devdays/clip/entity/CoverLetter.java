package our.devdays.clip.entity;

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
    @Temporal(TemporalType.TIMESTAMP)
    private Date uploadDate;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

}