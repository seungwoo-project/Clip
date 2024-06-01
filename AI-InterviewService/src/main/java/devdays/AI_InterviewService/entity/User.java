package devdays.AI_InterviewService.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity @Getter @Setter
@Table(name = "users")
public class User {
    @Id
    @Column(name = "userId")
    private String userId;

    @Column(name = "password", nullable = false)
    private String password;

    // 생성자, equals, hashCode 등 필요한 메서드
}