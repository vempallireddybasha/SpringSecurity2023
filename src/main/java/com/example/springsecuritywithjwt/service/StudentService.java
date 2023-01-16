package com.example.springsecuritywithjwt.service;

import com.example.springsecuritywithjwt.entity.Student;
import com.example.springsecuritywithjwt.repo.StudentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class StudentService  implements UserDetailsService {

    @Autowired
    private StudentRepository studentRepository;


    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<Student> student = studentRepository.findByName(userName);

        return student.map(CustomUserDetails::new).get();
    }

    public void saveStudent(Student student) {
        studentRepository.save(student);
    }

    public Student findStudent(String name) {
        return studentRepository.findByName(name).get();
    }

    public Student findStudentById(int id) {
       return  studentRepository.findById(id).get();
    }

    public List<Student> findAllStudents() {
        return studentRepository.findAll();
    }
}
