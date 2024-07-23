package com.cos.jwt.controller;

import com.cos.jwt.config.auth.PrincipalDetails;
import com.cos.jwt.model.User;
import com.cos.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequiredArgsConstructor
public class RestApiController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/home")
    public String home(){
        return "<h1>home</h1>";
    }

    @PostMapping("/token")
    public String token(){return "<h1>token</h1>";
    }

    // Tip : JWT를 사용하면 UserDetailsService를 호출하지 않기 때문에 @AuthenticationPrincipal 사용 불가능.
    // 왜냐하면 @AuthenticationPrincipal은 UserDetailsService에서 리턴될 때 만들어지기 때문이다.

    @PostMapping("/join")
    public String join(@RequestBody User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles("ROLE_USER");
        userRepository.save(user);
        return "회원가입완료";
    }

    // user, manager, admin 권한만 접근 가능
    @GetMapping("/api/v1/user")
    public String user(Authentication authentication){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("user() authentication : "+principalDetails.getUsername());
        return "user";
    }

    // ]manager, admin 권한만 접근 가능
    @GetMapping("/api/v1/manager")
    public String manager(){
        return "manager";
    }

    // admin 권한만 접근 가능
    @GetMapping("/api/v1/admin")
    public String admin(){
        return "admin";
    }

    // 유저 혹은 매니저 혹은 어드민이 접근 가능
//    @GetMapping("/api/v1/user")
//    public String user(Authentication authentication) {
//        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
//        System.out.println("principal : "+principal.getUser().getId());
//        System.out.println("principal : "+principal.getUser().getUsername());
//        System.out.println("principal : "+principal.getUser().getPassword());
//
//        return "<h1>user</h1>";
//    }
//
//    // 매니저 혹은 어드민이 접근 가능
//    @GetMapping("/api/v1/manager/reports")
//    public String reports() {
//        return "<h1>reports</h1>";
//    }
//
//    // 어드민이 접근 가능
//    @GetMapping("/api/v1/admin/users")
//    public List<User> users(){
//        return userRepository.findAll();
//    }


}
