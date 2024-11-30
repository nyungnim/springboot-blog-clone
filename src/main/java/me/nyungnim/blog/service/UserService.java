package me.nyungnim.blog.service;

import lombok.RequiredArgsConstructor;
import me.nyungnim.blog.domain.User;
import me.nyungnim.blog.domain.repository.UserRepository;
import me.nyungnim.blog.dto.AddUserRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // DTO를 받아 새로운 사용자를 저장, 저장한 고유ID를 반환하는 메서드
    public Long save(AddUserRequest dto) {
        // JPA의 save 메서드를 호출해 User 엔티티를 데이터베이스에 저장
        return userRepository.save(User.builder()
                .email(dto.getEmail())
                // 패스워드 암호화, 패스워드를 저장할 때 시큐리티를 설정하며 패스워드 인코딩용으로 등록한 빈을 사용해서 암호화한 후에 저장
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .build()).getId();
    }

    // 전달받은 유저 ID로 유저를 검색해서 전달
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }
}
