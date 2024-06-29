package devdays.AI_InterviewService.controller.validation;


import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegisterForm {
    @Id
    @NotBlank(message = "아이디는 필수 입력 항목입니다.")
    @Size(min = 4, max = 12, message = "아이디는 4자 이상 12자 이하로 입력해주세요.")
    private String userId;

    @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
    @Size(min = 4, max = 12, message = "비밀번호는 4자 이상 12자 이하로 입력해주세요.")
    private String password;
}
