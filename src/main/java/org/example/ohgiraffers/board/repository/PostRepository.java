package org.example.ohgiraffers.board.repository;

import org.example.ohgiraffers.board.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
