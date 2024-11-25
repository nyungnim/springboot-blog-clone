package me.nyungnim.blog.dto;

import lombok.Getter;
import me.nyungnim.blog.domain.Article;

/** 뷰에게 데이터를 전달하기 위한 객체
 * 요청을 받아 사용자에게 뷰를 보여주려면 뷰 컨트롤러가 필요
 * API 만들 때는 컨트롤러 메서드가 데이터를 직렬화한 JSON 문자열 반환
 * 뷰 컨트롤러 메서드는 뷰의 이름을 반환하고 모델 객체에 값을 담음
 * 반환값이 다를 뿐 구조는 비슷함
 */

@Getter
public class ArticleListViewResponse {

    private final Long id;
    private final String title;
    private final String content;

    public ArticleListViewResponse(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
    }
}
