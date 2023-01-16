package com.example.springsecuritywithjwt.entity;



import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Student {


@SequenceGenerator(
        name = "student_sequence",
        sequenceName = "student_sequence",
        allocationSize = 1

)
@GeneratedValue(
        strategy = GenerationType.AUTO,
        generator = "student_sequence"

)
@Id
    private int id;
    private String name;
    private String password;
    private int age;
    private String roles;


}
