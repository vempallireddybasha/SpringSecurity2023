package com.example.springsecuritywithjwt.controller;

import com.example.springsecuritywithjwt.entity.Student;
import com.example.springsecuritywithjwt.jwt.JwtFilter;
import com.example.springsecuritywithjwt.jwt.JwtRequest;
import com.example.springsecuritywithjwt.jwt.JwtResponse;
import com.example.springsecuritywithjwt.jwt.JwtUtil;
import com.example.springsecuritywithjwt.service.CustomUserDetails;
import com.example.springsecuritywithjwt.service.StudentService;

import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/student")
@RestController
public class StudentController {

    private static  final  String[] ADMIN_ACCESS={"ROLE_ADMIN","ROLE_MODERATOR"};
    private static  final  String[] MODERATOR_ACCESS={"ROLE_MODERATOR"};

    @Autowired
    private StudentService studentService;


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;
   @GetMapping("/hello")
    public  String hello(){
       System.out.println("inside hello method");
        return "Welcome to Spring Security";
    }

    @GetMapping("/validate")
   // @PreAuthorize("hasRole('ROLE_USER')")
    public  String validate(){
       // System.out.println("inside hello method");
        return "Welcome to Spring Security with JWT Token";
    }

    @PostMapping("/authenticate")

    public JwtResponse jwtsavestudent(@RequestBody JwtRequest  jwtRequest){
//        student.setRoles("ROLE_USER");
//        student.setPassword(passwordEncoder.encode(student.getPassword()));
//       Student student1= studentService.saveStudent(student);
        Authentication authenticate;
//        JwtRequest jwtRequest=new JwtRequest();
//        jwtRequest.setName(student1.getName());
//        jwtRequest.setPassword(student1.getPassword());
        try {
           authenticate = authenticationManager
                  .authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getName(), jwtRequest.getPassword()));
      }catch (Exception e){
            System.out.println(e.getMessage());
          throw new JwtException("Bad Credentials");

      }
       // Student student1 = studentService.findStudent(authenticate.getName());
        UserDetails userDetails = studentService.loadUserByUsername(authenticate.getName());
        String s = jwtUtil.generateToken(userDetails);
        return  new JwtResponse(s);
    }
    @PostMapping("/save")
    public String saveStudent(@RequestBody Student student){
        System.out.println(student);
       student.setRoles("ROLE_USER");
       student.setPassword(passwordEncoder.encode(student.getPassword()));
studentService.saveStudent(student);
return "Hi "+student.getName()+" Welcome to SpringSecurity Course";
    }
    @GetMapping("/access/{id}/{role}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MODERATOR')")
  public String giveAccessToUser(@PathVariable int id,@PathVariable String role,Principal principal){
       // String roles = getLoggedInStudent(principal).getRoles();
       Student student= studentService.findStudentById(id);
        List<String> activeRoles = getRolesForLoggedInUSer(principal);
        String newRole="";
        if (activeRoles.contains(role)){
           newRole= student.getRoles()+","+role;
           student.setRoles(newRole);
        }
       studentService.saveStudent(student);
        return "Hi "+student.getName()+" your role is  updated by"+principal.getName();
  }

  @GetMapping("/")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public List<Student> getAllstudents(){
       return studentService.findAllStudents();
  }

  public List<String> getRolesForLoggedInUSer(Principal principal){
    //  Student student = studentService.findStudent(principal.getName());
      String roles = getLoggedInStudent(principal).getRoles();
      List<String> AssignedRoles = Arrays.stream(roles.split(",")).collect(Collectors.toList());

      if(AssignedRoles.contains("ROLE_ADMIN")){
          return Arrays.stream(ADMIN_ACCESS).collect(Collectors.toList());
      }
      if(AssignedRoles.contains("ROLE_MODERATOR")){
          return Arrays.stream(MODERATOR_ACCESS).collect(Collectors.toList());
      }
      return Collections.emptyList();
  }

    

    public Student getLoggedInStudent(Principal principal){
     return studentService.findStudent(principal.getName());
  }

}
