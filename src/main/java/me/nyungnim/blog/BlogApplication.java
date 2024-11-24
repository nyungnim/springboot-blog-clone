package me.nyungnim.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// 스프링부트 어플리케이션임을 선언
@SpringBootApplication
public class BlogApplication {
    public static void main(String[] args) {
        // 스프링부트 어플리케이션 시작
        /* SpringApplication.run()
            1. 스프링컨텍스트 초기화 : 스프링이 관리하는 모든 빈(객체)을 생성하고 초기화함
            2. 자동 설정 활성화 : 스프링부트가 제공하는 기본 설정(내장 톰캣, 기본 포트 8080, DB 설정 ...)이 적용
            3. 내장 웹 서버 시작 : 애플리케이션이 HTTP 요청을 처리할 수 있도록 톰캣 같은 서버 실행
         */
        SpringApplication.run(BlogApplication.class, args);

        // args : 프로그램 시작 시 전달받은 인자
    }
}
