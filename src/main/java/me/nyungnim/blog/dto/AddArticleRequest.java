package me.nyungnim.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.nyungnim.blog.domain.Article;

@NoArgsConstructor  // 기본 생성자 자동 생성 -> new AddArticleRequest() 가능
@AllArgsConstructor // 모든 필드 값을 파라미터로 받는 생성자 추가 -> new AddArticleRequest("title", "content")
@Getter // request.getTitle()과 같이 값을 가져올 수 있음
public class AddArticleRequest {

    private String title;
    private String content;

    // 서비스 계층이나 데이터베이스 저장 작업에서 사용할 수 있도록 DTO를 JPA 엔터티로 변환하는 메서드
    // toEntity() : 추후에 블로그 글을 추가할 때 저장할 엔터티로 변환하는 용도
    public Article toEntity() { // 생성자를 사용해 객체 생성
        return Article.builder()
                .title(title)
                .content(content)
                .build();
    }
}
