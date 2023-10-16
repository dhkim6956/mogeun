//package com.mogun.backend.controller;
//
//import com.mogun.backend.domain.User.Repository.UserRepository;
//import com.mogun.backend.domain.User.User;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.access.annotation.Secured;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//@Slf4j
//@Controller
//@RequiredArgsConstructor
//public class indexController {
//
//    private final UserRepository userRepository;
//    private final BCryptPasswordEncoder bCryptPasswordEncoder;
//
//    @GetMapping({"", "/"})
//    public String index() {
//        return "index";
//    }
//
//    @GetMapping("/user")
//    public @ResponseBody String user() {
//        return "user";
//    }
//
//    @GetMapping("/admin")
//    public @ResponseBody String admin() {
//        return "admin";
//    }
//
//    @GetMapping("/manager")
//    public @ResponseBody String manage() { return "manager"; }
//
//
//    // Spring Security 가 해당 주소를 낚아 챈다.
//    @GetMapping("/loginForm")
//    public String loginForm() {
//        return "loginForm";
//    }
//
//    @GetMapping("/joinForm")
//    public String joinForm() {
//        return "joinForm";
//    }
//
//    @PostMapping("/join")
//    public @ResponseBody String join(User user) {
//        System.out.println(user);
//        String rawPassword = user.getPassword();
//        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
//        user.setPassword(encPassword);
//        user.setRole("ROLE_USER");
//
//        userRepository.save(user);
//        return "redirect:/loginForm";
//    }
//
//    @Secured("ROLE_ADMIN") // 주어진 권한을 가진 사람만 접근 가능
//    @GetMapping("/info")
//    public @ResponseBody String info() {
//        return "개인정보";
//    }
//
//    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
//    @GetMapping("/data")
//    public @ResponseBody String data() {
//        return "데이터 정보";
//    }
//
//    @GetMapping("/joinProc")
//    public @ResponseBody String joinProc() {
//        return "회원가입 완료";
//    }
//}
