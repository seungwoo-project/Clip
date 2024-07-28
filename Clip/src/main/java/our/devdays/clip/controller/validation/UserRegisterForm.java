package our.devdays.clip.controller.validation;


import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegisterForm {
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "아이디는 영어와 숫자로 입력해 주시기 바랍니다.")
    @Size(min = 4, message = "아이디는 최소 4자 이상이어야 합니다.")
    private String userId;

    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]*$",
            message = "비밀번호는 영어와 숫자를 포함해야 하며, 특수문자는 선택적으로 사용할 수 있습니다."
    )
    @Size(min = 4, message = "비밀번호는 최소 4자 이상이어야 합니다.")
    private String password;

    private String passwordConfirm;
}
