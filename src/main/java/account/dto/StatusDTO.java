package account.dto;
public class StatusDTO {
    public String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public StatusDTO(String status) {
        this.status = status;
    }
}
