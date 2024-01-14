package account.security;

import account.exception.CustomAccessDeniedHandler;
import account.exception.RestAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableWebSecurity
@SuppressWarnings("deprecation")
public class SecurityConfig {

    private final AuthenticationEntryPoint restAuthenticationEntryPoint;
    private final AccessDeniedHandler customAccessDeniedHandler;

    @Autowired
    public SecurityConfig(
            RestAuthenticationEntryPoint restAuthenticationEntryPoint,
            CustomAccessDeniedHandler customAccessDeniedHandler) {
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic()
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
                .csrf(AbstractHttpConfigurer::disable) // For Postman
                .headers(headers -> headers.frameOptions().disable()) // For the H2 console
                .authorizeHttpRequests(auth -> auth  // manage access
                        .requestMatchers(HttpMethod.POST, "/api/auth/signup").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/empl/payment/**").hasAnyRole("ACCOUNTANT", "USER", "SA")
                        .requestMatchers(HttpMethod.POST, "/api/auth/changepass/**").hasAnyRole("ACCOUNTANT", "USER", "ADMINISTRATOR", "SA")
                        .requestMatchers(HttpMethod.POST, "/api/acct/payments/**").hasAnyRole("ACCOUNTANT", "SA")
                        .requestMatchers(HttpMethod.PUT, "/api/acct/payments/**").hasAnyRole("ACCOUNTANT", "SA")
                        .requestMatchers(HttpMethod.GET, "/api/admin/user/**").hasAnyRole("ADMINISTRATOR", "SA")
                        .requestMatchers(HttpMethod.DELETE, "/api/admin/user/**").hasAnyRole("ADMINISTRATOR", "SA")
                        .requestMatchers(HttpMethod.PUT, "/api/admin/user/role/**").hasAnyRole("ADMINISTRATOR", "SA")
                        .requestMatchers(HttpMethod.PUT, "/api/admin/user/access/**").hasAnyRole("ADMINISTRATOR", "SA")
                        .requestMatchers(HttpMethod.GET, "/api/security/events/**").hasAnyRole("AUDITOR", "SA")
                        .requestMatchers(HttpMethod.GET, "/api/test/**").permitAll()
                        .anyRequest().permitAll())
                .sessionManagement(sessions -> sessions
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // no session
                .exceptionHandling().accessDeniedHandler(customAccessDeniedHandler);

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(13);
    }
}