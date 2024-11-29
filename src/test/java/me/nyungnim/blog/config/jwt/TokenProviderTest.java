package me.nyungnim.blog.config.jwt;

import io.jsonwebtoken.Jwts;
import me.nyungnim.blog.domain.User;
import me.nyungnim.blog.domain.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Duration;
import java.util.Date;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class TokenProviderTest {
    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProperties jwtProperties;

    @DisplayName("generateToken() : 유저 정보와 만료 기간을 전달해 토큰을 만들 수 있다.")
    @Test
    void generateToken() {
        // given : 토큰에 유저 정보를 추가하기 위한 테스트 유저를 만듬
        User testUser = userRepository.save(User.builder()
                .email("user@gmail.com")
                .password("test")
                .build());

        // when : 토큰 제공자의 generateToken() 메서드르 호출해 토큰을 만듭니다.
        String token =tokenProvider.generateToken(testUser, Duration.ofDays(14));

        // then : jjwt 라이브러를 사용해 토큰을 복호화한다. 토큰을 만들 때 클레임으로 넣어둔 id 값이 given 절에서 만든 유저 ID와 동일한지 확인
        Long userId = Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody()
                .get("id", Long.class);

        assertThat(userId).isEqualTo(testUser.getId());
    }

    @DisplayName("validToken() : 만료된 토큰인 때에 유효성 검증에 실패한다.")
    @Test
    void validToken_invalidToken() {
        // given : jjwt 라이브러리를 사용해 토큰을 생성
        String token = JwtFactory.builder()
                .expiration(new Date(new Date().getTime() - Duration.ofDays(7).
                        toMillis()))
                .build()
                .createToken(jwtProperties);
        // when : 토큰 제공자의 validToken() 메서드를 호출해 유효한 토큰인지 검증한뒤 결괏값을 반환받음
        boolean result = tokenProvider.validToken(token);

        // then : 반환값이 false(유효한 토큰이 아님)인 것을 확인
        assertThat(result).isFalse();
    }

    void validToken_valid() {
        // given : jjwt 라이브러리를 사용해 토큰을 생성
        String token = JwtFactory.withDefaultValues()
                .createToken(jwtProperties);

        // when : 토큰 제공자의 getAuthentication() 메서드를 사
        boolean result = tokenProvider.validToken(token);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("getAuthentication() : 토큰 기반으로 인증 정보를 가져올 수 있다.")
    @Test
    void getAuthentication() {
        // given
        String userEmail = "user@gmail.com";
        String token = JwtFactory.builder()
                .subject(userEmail)
                .build()
                .createToken(jwtProperties);

        // when
        Authentication authentication = tokenProvider.getAuthentication(token);
        // then
        assertThat(((UserDetails) authentication.getPrincipal()).getUsername()).isEqualTo(userEmail);
    }

    void getUserId() {
        // given
        Long userId = 1L;
        String token = JwtFactory.builder()
                .claims(Map.of("id", userId))
                .build()
                .createToken(jwtProperties);

        // when
        Long userIdByToken = tokenProvider.getUserId(token);

        // then
        assertThat(userIdByToken).isEqualTo(userId);
    }
}
