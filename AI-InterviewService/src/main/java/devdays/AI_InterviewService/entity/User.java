package devdays.AI_InterviewService.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity @Getter @Setter
@Table(name = "users")
public class User {
    @Id
    private String userId;
    private String password;

}