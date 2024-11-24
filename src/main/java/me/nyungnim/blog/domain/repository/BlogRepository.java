package me.nyungnim.blog.domain.repository;

import me.nyungnim.blog.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Article, Long> {
}
