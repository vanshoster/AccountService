package account.dto;

import account.validation.ValidPassword;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public class NewPasswordDTO {

    @NotBlank
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY, value = "new_password")
    @ValidPassword
    private String newPassword;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
