package com.example.springsecuritywithjwt.service;

import com.example.springsecuritywithjwt.entity.Student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomUserDetails  implements UserDetails {

    private String name;
    private int age;
     private  String password;
    private String roles;
    private List<GrantedAuthority> authorities;

    public CustomUserDetails(Student student){
        this.name=student.getName();
        this.age=student.getAge();
        this.password=student.getPassword();
        this.roles=student.getRoles();
        this.authorities= Arrays.stream(student.getRoles().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return  name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
