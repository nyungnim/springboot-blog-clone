package me.nyungnim.blog.service;


import lombok.RequiredArgsConstructor;
import me.nyungnim.blog.config.jwt.TokenProvider;
import me.nyungnim.blog.domain.User;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    // 전달받은 리프레시 토큰으로 토큰 유효성 검사 진행 -> 유효한 토큰일 때 리프레시 토큰으로 사용자 ID 찾기
    // 마지막으로는 사용자 ID로 사용자를 찾은 후 토큰 제공자의 generateToken() 메서드를 호출해서 새로운 액세스 토큰 생성
    public String createNewAccessToken(String refreshToken) {
        // 토큰 유효성 검사에 실패하면 예외 발생
        if (!tokenProvider.validToken(refreshToken)) {
            throw new IllegalArgumentException("Unexcepted token");
        }
        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
        User user = userService.findById(userId);

        return tokenProvider.generateToken(user, Duration.ofHours(2));
    }
}
