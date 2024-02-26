package org.example.ohgiraffers.board.repository;

import org.example.ohgiraffers.board.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

//Entity: 데이터베이스에서 정보를 저장하고 관리하기 위한 객체.



//JpaRepository 인터페이스를 Post엔티티와 함께 사용하는 것을 정의.
//*JpaRepository(SpringDataJPA에서 제공하는 인터페이스)
//데이터베이스와 상호 작용할 때 사용, 데이터를 생성하는 메서드를 제공(CRUD)

public interface PostRepository extends JpaRepository<Post, Long> {
}
//Post = 데이터베이스에 저장될 게시물을 나타내는 엔티티. (예, 게시물의 제목, 내용, 등의 정보를 포함)
//Long = 게시물의 고유한 식별자(ID)를 나타낸다. 이 ID는 각 게시물을 식별하기 위해 사용.

//요약: Post 엔티티와 관련된 CRUD 작업을 수행할 수 있는 JpaRepository를 정의. 이것을 사용하면 데이터베이스와의 상호 작용히 간편, 쿼리 작성할 필요 없이 간단한 메서드 호출을 통해 데이터를 다룰 수 있음.


