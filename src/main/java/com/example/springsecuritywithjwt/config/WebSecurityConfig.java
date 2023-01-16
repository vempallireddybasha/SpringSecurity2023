package com.example.springsecuritywithjwt.config;

import com.example.springsecuritywithjwt.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {
    @Autowired
    private StudentService studentService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests().requestMatchers(HttpMethod.GET ,"/student/hello").permitAll()
                .requestMatchers("/student/save").permitAll()
                // .requestMatchers("/student/authenticate").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();

        http.authenticationProvider(daoAuthenticationProvider());
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }


    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider authenticationProvider
                =new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(this.studentService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

//    @Bean
//
//    public InMemoryUserDetailsManager userDetailsManager() {
//        UserDetails userDetails = User.builder()
//                .username("vempalli")
//                .password(passwordEncoder().encode("basha"))
//                .roles("Admin")
//                .build();
//        System.out.println(passwordEncoder().encode("basha"));
//        return new InMemoryUserDetailsManager(userDetails);
//    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}


