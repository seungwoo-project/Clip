package devdays.AI_InterviewService.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity @Getter @Setter
@Table(name = "users")
public class User {
    @Id
    private String userId;
    private String password;

}