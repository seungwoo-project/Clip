package devdays.AI_InterviewService.controller.validation;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginForm {
    // 한글 검증
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "아이디에 한글을 포함할 수 없습니다.")
    private String userId;
    private String password;
}
