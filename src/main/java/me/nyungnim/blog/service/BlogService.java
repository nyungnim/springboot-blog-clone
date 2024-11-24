package me.nyungnim.blog.service;

import lombok.RequiredArgsConstructor;
import me.nyungnim.blog.domain.Article;
import me.nyungnim.blog.domain.repository.BlogRepository;
import me.nyungnim.blog.dto.AddArticleRequest;
import org.springframework.stereotype.Service;

/*
    서비스 계층
    - 데이터베이스 접근 로직을 캡슐화, 컨트롤러와 데이터베이스 간의 결합도를 낮춤
*/

@RequiredArgsConstructor    // final이 붙거나 @NotNull이 붙은 필드의 생성자 추가
@Service    // 빈으로 등록, 컨트롤러나 다른 서비스 클래스에서 이 클래스를 주입받아 사용 가능
public class BlogService {

    // 레포지토리 객체를 주입받아 데이터베이스와 상호작용
    private final BlogRepository blogRepository;

    // 블로그 글 추가 메서드, 매개변수 : 클라이언트로부터 전달받은 데이터를 포함하는 DTO
    public Article save(AddArticleRequest request) {
        // DTO 객체를 엔티티로 변환, 변환된 엔티티는 데이터베이스에 저장할 준비가 된다.
        // save() : JPA의 save() 메서드를 호출해 엔티티를 데이터베이스에 저장
        // return : 저장된 Article Entity 객체 반환
        return blogRepository.save(request.toEntity());
    }
}
