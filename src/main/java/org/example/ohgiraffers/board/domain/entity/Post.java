package org.example.ohgiraffers.board.domain.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

/** Entity (여기 코드에서는 게시물을 나타내는 객체인 Post 클래스를 의미. Post클래스는 데이터베이스의 게시물 테이블과 매핑되어 있고, 각 인스턴스는 데이터베이스의 한 행에 해당하는 게시물 정보를 담고 있다.
 * 실제 세계의 객체나 개념을 소프트웨어 내에서 모델링 한 것으로, 데이터 베이스의 테이블에 해당하는 클래스
 * 데이터베이스에서 정보를 저장하고 관리하기 위한 객체. (데이터베이스의 각 테이블과 매핑되는 객체를 의미)
 * 예) 게시물(Post)이라는 테이블이 있다면, 각 행은 게시물을 나타내는 엔티티 객체에 해당
 * 데이터베이스의 특정 테이블과 매핑되어 있고, 데이터베이스의 정보를 응용 프로그램에서 사용할 수 있도록 합니다.
 * 주의
 * 데이터의 일관성을 위해 setter 사용을 지양해야 한다.
 * setter를 사용한다면, 어디에서든 Entity를 수정해버릴 수 있기 때문에 일관성을 유지하기 어렵다.
 */

/**
 * @Data -> getter, setter, toString, equalsandhashcode . .
 */
//Entity : JPA Entity라는 것을 의미한다. Id와 @GeneratedValue를 이용해 고유 식별자를 정의한다.
@Entity
//@Builder : 빌더 디자인 패턴을 자동으로 생성해준다.
@Builder
//@Getter : 클래스의 모든 필드에 대한 getter 메서드를 자동으로 생성해준다.
@Getter
//@AllArgsConstructor : 클래스의 모든 필드를 매개변수로 받는 전체 생성자를 자동 생성한다.
//모든 필드를 사용하여 객체를 만들 수 있는 생성자를 자동으로 만들어준다.
@AllArgsConstructor
//@NoArgsConstructor : 매개변수가 없는 기본 생성자를 생성한다.
//어떠한 필드도 초기화하지 않는 기본 생성자를 자동으로 문들어준다.
//accessLevel를 통해 접근 수준을 설정 할 수 있다.
@NoArgsConstructor( access = AccessLevel.PROTECTED)
public class Post {

    //@Id & @GeneratedValue 데이터베이스의 테이블에서 기본키를 정의할 떄 사용
    @Id //해당 필드가 엔티티의 기본 키를 나타냄. 데이터베이스 테이블에서 각 행을 식별하는데 사용.
    @GeneratedValue //기본 키의 값을 자동으로 생성하기 위해 사용. 데이터베이스가 자동으로 증가하는 값을 생성하도록 설정. (숫자 형태)


    private Long postId;    //postId는 엔티티의 기본키 및 고유한 식별자, 데이터베이스에서 이 값은 자동으로 생성+증가

    private String title;
    private String content;

    //메서드: 게시물의 제목+내용을 업데이트하는 기능을 담당. 호출 시 게시물의 제목+내용을 새로운 값으로 업데이트 할 수 있음.
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

}


