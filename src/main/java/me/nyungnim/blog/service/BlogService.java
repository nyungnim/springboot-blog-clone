package me.nyungnim.blog.service;

import lombok.RequiredArgsConstructor;
import me.nyungnim.blog.domain.Article;
import me.nyungnim.blog.domain.repository.BlogRepository;
import me.nyungnim.blog.dto.AddArticleRequest;
import me.nyungnim.blog.dto.UpdateArticleRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public List<Article> findAll() {
        // JPA 지원 메서드인 findAll()을 호출해 article 테이블에 저장되어 있는 모든 데이터를 조회
        return blogRepository.findAll();
    }

    public Article findById(long id) {
        // JpaRepository가 제공하는 메서드, DB에서 ID를 기준으로 데이터 조회
        /** 반환값
         * - Optional<Article> - 조회 결과를 감싸는 Optional 객체
         * - 데이터가 존재하면 Article 객체를 포함
         * - 데이터 없으면 비어있는 Optional을 반환
         */
        return blogRepository.findById(id)
                // Optional에 값이 있으면 반환 없으면 예외 던짐
                // 아래 예외 : 잘못된 매개변수가 입력되었음을 나타냄
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));
    }

    // 블로그 글의 ID를 받은 뒤 JPA에서 제공하는 deleteById() 메서드를 이용해 DB에서 데이터 삭제
    public void delete(long id) {
        blogRepository.deleteById(id);
        // id에 해당하는 데이터가 없으면 EmptyResultDataAccessException 예외를 던짐
    }

    // repository를 사용해 글 수정
    // 매칭한 메서드를 하나의 트랜잭션으로 묶는 역할
    @Transactional
    public Article update(long id, UpdateArticleRequest request) {
        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));

        article.update(request.getTitle(), request.getContent());

        return article;
    }
}
