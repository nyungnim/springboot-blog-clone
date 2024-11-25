package me.nyungnim.blog.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/*
    왜 Getter만 제공하는가
    - 엔티티의 데이터는 가능하면 불변(Immutable)하게 유지하는 것이 좋다.
    - Setter를 제공하면 외부에서 데이터를 임의로 수정할 수 있어 문제가 생길 수 있음
*/

// 엔터티의 생성 및 수정 시간을 자동으로 감시하고 기록하기 위한 애너테이션 추가
@EntityListeners(AuditingEntityListener.class)
// JPA 엔티티임을 나타냄, JPA가 이 클래스를 기반으로 테이블을 생성하거나 조작한다.
// 테이블명을 변경하고 싶으면 @Table(name="") 사용
@Entity
// 롬복 애노테이션을 사용해 코드를 반복 입력할 필요가 없어짐
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article {

    // id 필드를 기본키로 지정
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본키 자동생성, identity -> 자동 값 증가
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Builder    // 빌더 패턴으로 객체 생성
    public Article(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    @CreatedDate // 엔터티가 생성될 때 생성시간 저장
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate   // 엔터티가 수정될 때 수정시간 저장
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /*
        코드 변경을 한 이유 (아래 코드 전체 주석)
        => Lombok 애노테이션을 활용하여 코드의 간결성과 가독성 높임
        1. 기존에는 각 필드에 대해 직접 Getter 메서드를 작성하였으나 Lombok의 Getter를 활용해 자동으로 Getter 메서드 생성
        2. protected Article() {}라는 기본생성자를 직접 생성하는 Lombok의 NoArgsConstructor 사용
        - access = AccessLevel.PROTECTED : 기본 생성자를 protected로 제한, 외부에서 사용하지 않도록 제한
    */

//    protected Article() {   // JPA는 내부적으로 기본 생성자가 필요, protected로 설정해 외부 클래스에 직접 호출 못하도록 제한
//    }
//
//    // 게터
//    public Long getId() {
//        return id;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public String getContent() {
//        return content;
//    }
}
