package me.nyungnim.blog.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Table(name="users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
// Spring Security에서 사용자 인증 객체로 사용되기 위한 인터페이스
public class User implements UserDetails {  // UserDetails를 상속받아 인증 객체로 사용

    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    // 이메일과 비밀번호를 입력받아 사용자 객체 생성
    @Builder
    public User(String email, String password, String auth) {
        this.email = email;
        this.password = password;
    }

    @Override // 사용자 권한 반환 -> 단일 권한 "user" 반환
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // SimpleGrantedAuthority : Spring Security에서 권한을 표현하는 클래스
        return List.of(new SimpleGrantedAuthority("user"));
    }

    // 사용자의 id를 반환 (고유한 값)
    @Override
    public String getUsername() {
        return email;
    }

    // 사용자의 패스워드 반환, Spring Security는 이 값을 기반으로 사용자 인증 수행
    @Override
    public String getPassword() {
        return password;
    }


    // 여기서부터의 boolean 타입 반환 메서드들은 계정 상태 관련 메서드
    // 모든 메서드가 true를 반환, 기본적으로 계정은 항상 사용 가능한 상태로 설정
    // 필요에 따라 이 로직을 수정해서 계정 상태를 실제 데이터베이스 필드로 관리 가능
    // 계정 만료 여부
    @Override
    public boolean isAccountNonExpired() {
        // 만료되었는지 확인하는 로직
        return true;    // true -> 만료되지 않았음
    }

    // 계정 잠금 여부 반환
    @Override
    public boolean isAccountNonLocked() {
        // 계정 잠금되었는지 확인하는 로직
        return true;    // true -> 잠금되지 않았음
    }

    // 패스워드의 만료 여부 반환
    @Override
    public boolean isCredentialsNonExpired() {
        // 패스워드가 만료되었는지 확인하는 로직
        return true;    // true -> 만료되지 않았음
    }

    // 계정 사용 가능 여부 반환
    @Override
    public boolean isEnabled() {
        // 계정이 사용 가능한지 확인하는 로직
        return true;    // true -> 사용 가능
    }
}
