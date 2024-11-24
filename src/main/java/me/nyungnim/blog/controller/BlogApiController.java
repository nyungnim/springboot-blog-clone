package me.nyungnim.blog.controller;


import lombok.RequiredArgsConstructor;
import me.nyungnim.blog.domain.Article;
import me.nyungnim.blog.dto.AddArticleRequest;
import me.nyungnim.blog.service.BlogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor    // final이 붙은 필드를 매개변수로 받는 생성자 자동 생성, 의존성을 생성자 주입 방식으로 설정 가능 ex: BlogService를 자동으로 주입받는 생성자 추가
@RestController // HTTP Response Body에 객체 데이터를 JSON 형식으로 반환하는 컨트롤러
public class BlogApiController {

    // 서비스 계층 객체를 주입 받아 사용
    private final BlogService blogService;


    // 클라이언트에서 새로운 블로그 글을 추가하는 요청
    // HTTP 메서드가 POST 일 때, 전달받은 URL과 동일하면 메서드로 매핑
    @PostMapping("/api/articles")
    // @RequestBody : 클라이언트가 보낸 JSON 데이터를 AddArticleRequest DTO 객체로 매핑
    // ResponseEntity<Article> : HTTP 응답을 생성하는 객체 : 상태코드 & 응답 데이터(Article 엔터티) 함께 반환
    public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest request) {
        // 서비스 계층의 메서드를 호출 -> 요청 데이터 처리 -> DTO를 엔터티로 변환한 뒤에 데이터베이스에 저장
        Article savedArticle = blogService.save(request);

        // 요청한 자원이 성공적으로 생성되었으며 저장된 블로그 글 정보를 응답 객체에 담아 전송
        // HTTP 상태코드 201 : 새로운 리소스가 성공적으로 생성되었음을 의미함
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedArticle);
    }
    // 과정 정리 :JSON 데이터 -> DTO 객체로 매핑 -> DTO를 엔터티로 변환 -> 데이터베이스에 저장 -> 요청 성공 HTTP 상태코드 & 저장된 엔터티를 JSON으로 클라이언트에 반환
}
