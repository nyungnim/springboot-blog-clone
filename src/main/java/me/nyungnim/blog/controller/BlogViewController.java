package me.nyungnim.blog.controller;

import lombok.RequiredArgsConstructor;
import me.nyungnim.blog.domain.Article;
import me.nyungnim.blog.dto.ArticleListViewResponse;
import me.nyungnim.blog.dto.ArticleViewResponse;
import me.nyungnim.blog.service.BlogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;    // Spring MVC에서 제공하는 Model 인터페이스의 경로
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BlogViewController {

    private final BlogService blogService;

    @GetMapping("/articles")
    // Model 객체는 컨트롤러에서 뷰로 데이터를 넘길 때 "키-쌍" 값으로 데이터를 추가한다.
    public String getArticles(Model model) {
        // 자바 stream api와 DTO 관점에서 보기, stream : 자바의 데이터 처리 api, 컬렉션 데이터를 필터링하거나 매핑(변환)하는 작업을 간결하게 처리 가능
        List<ArticleListViewResponse> articles = blogService.findAll().stream()
                // Stream의 각 Article 객체를 ArticleListViewResponse 클래스의 생성자를 참조하여 객체로 변환
                .map(ArticleListViewResponse::new)
                // 변환된 스트림 데이터를 다시 리스트로 변환 -> 자바 16이상에서 사용 가능
                // 자바 8  사용 시 .collect(Collectors.toList())로 대체
                .toList();
        model.addAttribute("articles", articles);   // 블로그 글 리스트 저장

        // 뷰 리졸버가 articleList라는 이름의 템플릿(articleList.html 같은)을 찾아 렌더링한다.
        return "articleList";
    }

    @GetMapping("/articles/{id}")
    public String getArticle(@PathVariable("id") long id, Model model) {
        Article article = blogService.findById(id);
        model.addAttribute("article", new ArticleViewResponse(article));

        return "article";
    }

    /** 동작 방식
     * 1. 클라이언트가 /articles로 GET 요청을 보냄
     * 2, 컨트롤러가 blogService.findAll()을 호출하여 블로그 글 데이터를 가져옴
     * 3. 가져온 데이터를 ArticleListViewResponse DTO로 반환
     * 4. 변환된 데이터 리스트를 Model 객체에 추가
     * 5. 뷰 이름 articleList를 반환하여 해당 뷰에서 데이터를 렌더링
     */

    @GetMapping("/new-article")
    // long 타입은 원시타입으로 null 값을 가질 수 없기 때문에 id가 비어있거나 전달되지 않으면 예외가 발생한다.
    // id키를 가진 쿼리 파라미터의 값을 id 변수에 매핑하였다 -> Id가 없을 수도 있기 때문이다. 따라서 null을 허용해야하기 때문에 Long을 사용해준다.
    // value="id" 사용하는 이유는 HTTP 요청의 쿼리 파라미터 중 id 라는 이름의 값을 매핑한다.
    public String newArticle(@RequestParam(value="id", required = false) Long id, Model model) {
        if (id == null) {   // id가 없으면 생성
            model.addAttribute("article", new ArticleViewResponse());
        } else {    // id가 있으면 수정
            Article article = blogService.findById(id);
            model.addAttribute("article", new ArticleViewResponse(article));
        }
        return "newArticle";
    }
}
