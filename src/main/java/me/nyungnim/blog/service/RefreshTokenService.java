package me.nyungnim.blog.service;

import lombok.RequiredArgsConstructor;
import me.nyungnim.blog.domain.RefreshToken;
import me.nyungnim.blog.domain.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;


// 전달받은 리프레시 토큰으로 리프레시 토큰 객체를 검색해서 전달하는 메서드 구현

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected token"));
    }
}
