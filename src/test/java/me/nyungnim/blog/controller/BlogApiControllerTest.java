package me.nyungnim.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.nyungnim.blog.domain.Article;
import me.nyungnim.blog.domain.repository.BlogRepository;
import me.nyungnim.blog.dto.AddArticleRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** 코드 전체 흐름
 * 1. 테스트 환경 준비
 * - 스프링 애플리케이션 컨텍스트 로드 + MockMvc 설정
 * - 데이터베이스 초기화 작업 수행
 * 2. addArticle() 테스트
 * - 블로그 글 추가 API(/api/articles) 호출
 * - HTTP 요청 시뮬레이션, 요청 결과와 데이터베이스 상태를 검증
 */

// 컨트롤러, 서비스, 레포지토리 등 모든 빈을 로드
@SpringBootTest // 테스트용 애플리케이션 컨텍스트 로드, 애플리케이션 전체 환경에서 통합 테스트 수행 시에 사용
@AutoConfigureMockMvc   // MockMvc 객체를 자동으로 구성하고 주입
class BlogApiControllerTest {

    // 테스트 클래스와 하위 테스트 클래스에서도 사용 가능하도록 protected로 선언
    @Autowired
    protected MockMvc mockMvc; // MockMvc 컨트롤러 계층 테스트를 위한 객체, HTTP 요청을 시뮬레이션하고 응답을 검증함


    // 다른 테스트 클래스에서도 직렬화/역직렬화가 필요할 가능성이 크므로, protected로 선언
    // JSON 데이터를 Java 객체로 변환 or Java 객체를 JSON으로 변환
    @Autowired
    protected ObjectMapper objectMapper;    // 직렬화, 역직렬화를 위한 클래스

    // 컨트롤러, 서비스, 레포지토리 등 모든 빈을 포함하는 웹 컨텍스트
    @Autowired
    private WebApplicationContext context;  // 스프링의 애플리케이션 컨텍스트를 주입 받음

    // 테스트 전후로 데이터베이스를 초기화 하거나 상태를 확인
    @Autowired
    BlogRepository blogRepository;  // 데이터베이스와 상호작용하는 레포지토리 빈을 주입

    @BeforeEach // 각 테스트 메서드 실행 전에 반드시 실행
    public void mockMvcSetUp() {
        // WebApplicationContext를 기반으로 MockMvc를 초기화, 테스트 중 실제 컨트롤러 빈과 매핑되는 MockMvc 설정
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
        // 테스트 실행 전 데이터베이스를 비움
        // 테스트 간 데이터 간섭 방지 -> 독립적인 테스트 보장
        blogRepository.deleteAll();
    }

    @DisplayName("addArticle: 블로그 글 추가에 성공한다.")
    @Test
    public void addArticle() throws Exception {
        // given : 테스트 데이터 준비
        final String url = "/api/articles";
        final String title = "title";
        final String content = "content";
        final AddArticleRequest userRequest = new AddArticleRequest(title, content);

        // 객체 JSON으로 직렬화
        final String requestBody = objectMapper.writeValueAsString(userRequest);

        // when : API 호출
        // 요청의 결과를 ResultActions 객체로 반환받아 이후 검증에 사용
        ResultActions result = mockMvc.perform(post(url)
                // 요청 본문이 JSON 형식임을 지정
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                // JSON으로 변환된 요청 데이터를 본문에 포함
                .content(requestBody));

        // then : 응답 및 데이터 검증
        // HTTP 상태 코드가 201 Created인지 확인, 컨트롤러가 성공적으로 글을 저장했음을 나타냄
        result.andExpect(status().isCreated());

        // 데이터베이스에 저장된 데이터를 가져옴
        List<Article> articles = blogRepository.findAll();

        assertThat(articles.size()).isEqualTo(1);
        assertThat(articles.get(0).getTitle()).isEqualTo(title);
        assertThat(articles.get(0).getContent()).isEqualTo(content);
    }
}