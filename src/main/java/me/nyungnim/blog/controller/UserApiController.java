package me.nyungnim.blog.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.nyungnim.blog.dto.AddUserRequest;
import me.nyungnim.blog.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class UserApiController {

    private final UserService userService;

    @PostMapping("/user")
    public String signup(AddUserRequest request) {
        userService.save(request);  // 회원가입 메서드 호출
        return "redirect:/login";   // 회원 가입이 완료된 이후에 로그인 페이지로 이동
    }


    // 현재 인증된 사용자의 세션을 무효화하고 로그아웃 처리 후 로그인 페이지로 리다이렉트 하는 것
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        // Spring Security에서 제공하는 로그아웃 처리기
        // 현재 인증된 사용자의 세션을 무효화, SecurityContext를 비우며 로그아웃 처리
        new SecurityContextLogoutHandler().logout(request, response,
                // 현재 인증된 사용자의 정보를 가져온다. 인증 객체(Authentication)는 사용자의 인증 상태를 나타내고 로그아웃 시 이를 제거
                SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/login";
    }
}
