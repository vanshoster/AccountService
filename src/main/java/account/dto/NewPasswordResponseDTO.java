package account.dto;

public class NewPasswordResponseDTO {
    private String email;

    private String status;

    public NewPasswordResponseDTO(String email) {
        this.email = email;
        this.status = "The password has been updated successfully";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
