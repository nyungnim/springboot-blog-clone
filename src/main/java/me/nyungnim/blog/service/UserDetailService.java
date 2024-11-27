package me.nyungnim.blog.service;

import lombok.RequiredArgsConstructor;
import me.nyungnim.blog.domain.User;
import me.nyungnim.blog.domain.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
//스프링 시큐리티에서 사용자 정보를 가져오는 인터페이스, 사용자 인증 처리 시 사용된다.
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    // 사용자 이름(email)으로 사용자의 정보를 가져오는 메서드
    public User loadUserByUsername(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException((email)));
    }
}
