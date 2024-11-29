package me.nyungnim.blog.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import me.nyungnim.blog.domain.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class TokenProvider {

    private final JwtProperties jwtProperties;


    // 사용자 정보와 만료기간을 받아 토큰 생성
    public String generateToken(User user, Duration expiredAt) {
        Date now = new Date();
        // makeToken 메서드를 호출해 토큰 생성
        return makeToken(new Date(now.getTime() + expiredAt.toMillis()), user);
    }

    // JWT 토큰 생성 메서드 : 인자로 만료시간, 유저 정보를 받음
    private String makeToken(Date expiry, User user) {
        Date now = new Date();

        // JWT 구성요소
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)   // 헤더 typ: JWT
        // 내용 iss : ajufresh@gmail.com(properties 파일에서 설정한 값)
                .setIssuer(jwtProperties.getIssuer())   // iss : 토큰 발급자 정보
                .setIssuedAt(now)   // 내용 iat : 현재 시간, 토큰 발급 시간
                .setExpiration(expiry)  // 내용 exp : expiry 멤버 변숫값, 토큰 만료 시간
                .setSubject(user.getEmail())    // 내용 sub : 유저의 이메일, 사용자 식별 정보
                .claim("id", user.getId())  // 클레임 id : 유저 ID
                // 서명 : 비밀값과 함께 해시값을 HS256 방식으로 암호화, 비밀키를 이용
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                // 최종적으로 토큰을 Base64 URL-safe 형식으로 인코딩하여 문자열 반환
                .compact();
    }

    // JWT 토큰 유효성 검증 메서드
    public boolean validToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey())    // 비밀값으로 복호화
                    // 서명 및 만료 기간 검증, 예외 발생 시(서명이 유효하지 않거나 만료된 경우) false 반환
                    .parseClaimsJws(token);
            return true;
        } catch(Exception e) {  // 복호화 과정에서 에러가 나면 유효하지 않은 토큰
            return false;
        }
    }

    // JWT 토큰을 기반으로 Spring Security 인증 객체 생성
    public Authentication getAuthentication(String token) {
        // Claims -> 사용자 정보 담겨있음, getClaims 메서드 호출 -> 토큰에서 Claims(Payload 부분)를 가져옴
        Claims claims = getClaims(token);
        // 모든 사용자에게 기본 권한 ROLE_USER를 부여
        // Spring Security에서는 권한을 표현할 때 GrantedAuthority 인터페이스를 사용, SimpleGrantedAuthority는 이를 구현한 클래스, 중복허용x -> Set 인터페이스
        // Collections.singleton : Set을 생성하는 편의 메서드, 하나의 원소만 가지는 불변 Set을 만듬
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        // 3가지 정보(사용자 정보, 인증 자격 증명(여기서는 빈 문자열), 사용자 권한 목록) 포함
        return new UsernamePasswordAuthenticationToken(new org.springframework.security.core.userdetails.User(claims.getSubject(),
                "", authorities), token, authorities);

    }

    // 토큰 기반으로 유저 ID를 가져오는 메서드
    public Long getUserId(String token) {
        Claims claims = getClaims(token);
        return claims.get("id", Long.class);
    }

    private Claims getClaims(String token) {
        return Jwts.parser()    // 클레임 조회, JWT 파서를 생성
                .setSigningKey(jwtProperties.getSecretKey()) // 토큰을 검증하기 위한 비밀키 설정
                .parseClaimsJws(token)  // 토큰을 디코딩하고 서명을 검증
                .getBody(); // JWT 토큰의 Payload 부분을 파싱해 Claims 객체로 반환
    }
}
