package account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.web.context.WebServerPortFileWriter;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AccountServiceApplication {

    @Bean
    public WebServerPortFileWriter webServerPortFileWriter() {
        return new WebServerPortFileWriter();
    }

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(AccountServiceApplication.class);
        application.addListeners(new ApplicationPidFileWriter());
        application.run(args);
    }

}
