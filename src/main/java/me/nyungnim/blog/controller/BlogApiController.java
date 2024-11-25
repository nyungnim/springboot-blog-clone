package me.nyungnim.blog.controller;


import lombok.RequiredArgsConstructor;
import me.nyungnim.blog.domain.Article;
import me.nyungnim.blog.dto.AddArticleRequest;
import me.nyungnim.blog.dto.ArticleResponse;
import me.nyungnim.blog.dto.UpdateArticleRequest;
import me.nyungnim.blog.service.BlogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/api/articles")
    // 응답 본문에 ArticleResponse(DTO) 객체들의 리스트 포함
    public ResponseEntity<List<ArticleResponse>> findAllArticles() {
        List<ArticleResponse> articles = blogService.findAll()
                .stream()  // 조회된 엔터티 리스트를 Stream으로 변환하여 데이터를 가공할 준비
                .map(ArticleResponse::new) // 생성자를 호출해 각 Article 엔티티를 ArticleResponse DTO로 변환 작업
                .toList();  // Stream 작업을 통해 변환된 데이터를 다시 리스트로 변환
        return ResponseEntity.ok()
                .body(articles);
    }

    @GetMapping("/api/articles/{id}")
    // 매개변수 이름 손실 문제가 발생 -> @PathVariable 이름 명시적으로 변경
    public ResponseEntity<ArticleResponse> findArticle(@PathVariable("id") long id) {
        Article article = blogService.findById(id);

        return ResponseEntity.ok()
                .body(new ArticleResponse(article));
    }

    @DeleteMapping("/api/articles/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable("id") long id) {
        blogService.delete(id);

        return ResponseEntity.ok()
                .build();
        /** build가 여기서 사용되는 이유
         * ResponseEntity를 직접 생성할 때, 추가적인 본문 데이터가 필요 없을 경우 사용된다.
         * ResponseEntity 객체를 본문이 없는 상태로 반환
         */
    }

    @PutMapping("/api/articles/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable("id") long id, @RequestBody UpdateArticleRequest request) {
        Article updatedArticle = blogService.update(id, request);

        return ResponseEntity.ok()
                .body(updatedArticle);
    }
}
