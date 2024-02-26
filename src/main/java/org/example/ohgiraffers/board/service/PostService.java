package org.example.ohgiraffers.board.service;



import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.ohgiraffers.board.domain.dto.*;
import org.example.ohgiraffers.board.domain.entity.Post;
import org.example.ohgiraffers.board.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Service를 인터페이스와 구현체로 나누는 이유
 * 1. 다형성과 OCP 원칙을 지키기 위해
 * 인터페이스와 구현체가 나누어지면, 구현체는 외부로부터 독립되어, 구현체의 수정이나 확장이 자유로워진다.
 * 2. 관습적인 추상화 방식
 * 과거, Spring 에서 AOP를 구현 할때 JDK Dynamic Proxy를 사용했는데, 이때 인터페이스가 필수였다.
 * 지금은, CGLB를 기본적으로 포함하여 클래스 기반을 프록시 객체를 생성 할 수 있게 되었다.
 */

/** @Transactional
 * 선언적으로 트랜잭션 관리를 가능하게 해준다.
 * 메서드가 실행되는 동안 모든 데이터베이스 연산을 하나의 트랜잭션으로 묶어 처리한다.
 * 이를통해, 메서드 내에서 데이터베이스 상태를 변경하는 작업들이 모두 성공적으로 완료되면 그 변경사항을 commit하고
 * 하나라도 실패하면 모든 변경사항을 rollback 시켜 관리한다.
 *
 * Transaction
 * 데이터베이스의 상태를 변화시키기 위해 수행하는 작업의 단위
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor

public class PostService {  //게시물 관련 로직을 처리하는 서비스 클래스. 게시물 생성, 조회, 수정,삭제 기능 구현

    private final PostRepository postRepository; //PostRepository는 데이터베이스에 접근하여 게시물에 관련된 작업을 수행하는 클래스.
//private final: 클래스가 다른 곳에서 변경될 수 없는 상태를 유지.
    @Transactional //트랜잭션의 특성을 지원하기 위해 메서드나 클래스에 붙여 사용됨. 이를 통해 트랜잭션의 시작과 종료를 자동으로 처리 할 수 있음. 메서드 내에서 모든작업은 하나의 트랜잭션으로 묶이고 메서드 실행 도중 예외가 발생하면 이전 상태로 롤백.
    //트랜잭션: 데이터베이스에서 여러 작업이 하나로 묶여 원자적으로 실행되도록 하는 개념. (트랜잭션 안에 있는 모든 작업이 성공하거나 실패시 모두 함께 성공하거나 실패한다는 것을 의미)
    //어떤 작업 하나라도 실패시 이전의 상태로 롤백되어야 함.
    public CreatePostResponse createPost(CreatePostRequest request) {
//CreatePostRequest를 받아서 'CreatePostResponse를 반환하는 create메서드=새로운 게시물을 생성
        Post post = Post.builder()  //Post라는 클래스에 대한 빌더를 생성, 빌더를 사용해 게물 객체를 말들 수 있음.
                .title(request.getTitle()) //요청에서 제목을 가져오고 게시물의 제목으로 설정
                .content(request.getContent())  //요청에서 내용가져오고 게시물의 내용으로 설정
                .build();   //설정한 제목과 내용으로 게시물 객체를 완성해 반환.

        Post savePost = postRepository.save(post); //완성된 게시물 객체를 데이터베이스에 저장. postRepository는 데이터베이스에 접근하기 위한 객체.

        return new CreatePostResponse(savePost.getPostId(), savePost.getTitle(), savePost.getContent());
//저장된 게시물의 아이디, 제목, '내용을 CreatePostResponse'객체를 생성해 반환. 이 응답을 클라이언트에게 새로 생성된 게시물에 대한 정보를 전달.
        //요약: 이 코드는 새로운 게시물을 생성, 데이터베이스에 저장, 해당 게시물의 정보를 포함한 응답을 반환하는 기능을 수행.
    }


    public ReadPostResponse readPostById(Long postId) { //특정 게시물 ID를 사용하여 게시물을 읽어오는 메서드를 정의

        Post foundPost = postRepository.findById(postId) //postRepository를 사용해 주어진 id에 해당하는 게시물을 데이터베이스에서 찾는다.
                    .orElseThrow(() -> new EntityNotFoundException("해당 postId로 조회된 게시물이 없습니다.")); //orElseThrow() = Optional객체가 비어있을 때 실행할 동작을 정의, 특정 예외를 발생시키는 역할.
//만약 게시물을 찾지 못하면, 'EntityNotFoundException'을 (람다표현식) 던져서 해당 게시물이 없음을 나타냄. Optional(값이 있을 수도/없을 수도 있는 컨테이너 객체)객체가 비어있을 때 실행.
        //요약: Optional 객체가 비어있을 때 "해당 postId로 조회된 게시물이 없습니다" 메세지를 포함한 EntityNotFoundException을 발생시키는 역할을 한다.
        return new ReadPostResponse(foundPost.getPostId(), foundPost.getTitle(), foundPost.getContent());
    }//찾은 게시물의 정보를 사용하여 ReadPostResponse객체를 생성하고 반환. 이 응답을 클라이언트에 전달.
    //요약: 이 메서드는 주어진 게시물ID를 사용하여 해당ID에 해당하는 게시물을 읽어오는 기능을 수행.
    //게시물이 데이터베이스에 없으면 'EntityNotFoundException'을 예외 발생, 있으면 데이터베이스에서 게시물을 가져와 'ReadPostResponse객체로 만들어 반환. 이 정보를 클라이언트에 전달.
    @Transactional
    public UpdatePostResponse updatePost(Long postId, UpdatePostRequest request) { //메서드: 게시물을 업데이트하는데 사용. 게시물의 id와 업데이트에 필요한 정보가 제공. 업데이트된 게시물의 정보를 포함한 응답을 반환.

        Post foundPost = postRepository.findById(postId)    //주어진id로 데이터베이스에서 해당 게시물을 찾는다.
                .orElseThrow(() -> new EntityNotFoundException("해당 postId로 조회된 게시글이 없습니다."));
// 만약 게시물을 못 찾으면, EntityNotFoundException을 던져서 해당 게시물이 없음을 나타냄.

        //Dirty Checking
        foundPost.update(request.getTitle(), request.getContent());
        //게시물 객체의 update()메서드를 호출하여 제목과 내용을 업데이트. 이 메서드는 게시물 객체의 상태를 변경.(Dirty Checking과정)

        return new UpdatePostResponse(foundPost.getPostId(), foundPost.getTitle(), foundPost.getContent());
    //업데이트된 게시물의 정보를 사용하여 UpdatePostResponse 객체를 생성하고 반환. 이 객체에는 업데이트된 게시물의 정보가 포함되어 있음.
    } //요약: 특정 id를 사용하여 게시물을 업데이트, 주어진 id로 데이터베이스에서 게시물을 찾는데 없다면, 예외를 발생. 있다면 새로운 정보로 게시물을 업데이트하고, 업데이트된 게시물의 정보를 반환.

    @Transactional  //특정id를 사용해 게시물을  삭제하는 기능을 구현.
    public DeletePostResponse deletePost(Long postId) {
//메서드: 게시물을 삭제하는 역할, 삭제할 게시물의 id가 매개변수(Long postId)로 전달받아 해당하는 게시물을 삭제.

            Post foundPost = postRepository.findById(postId)//주어진 id로 데이터베이스에서 해당 게시물을 찾는다.
                    .orElseThrow(() -> new EntityNotFoundException("해당 postId로 조회된 게시글이 없습니다."));
    //게시물이 없다면, EntityNotFoundException을 발생 (없음을 나타냄)
        postRepository.delete(foundPost);   //찾은 게시물을 데이터베이스에서 삭제.

        return new DeletePostResponse(foundPost.getPostId());
        }   //삭제된 게시물의 id를 사용해 DeletePostResponse(응답 객체: 삭제된 게시물의 id와 같은 정보를 포함)를 생성하고 반환. 클라이언트에게 삭제 작업이 완룐되었음을 알려주는 역할.


    //메서드: 모든 게시물을 읽어오는 기능을 구현
    public Page<ReadPostResponse> readAllPost(Pageable pageable) {
        //메서드: (페이지네이션된 형태)로 모든 게시물을 읽어오는 역할. 한번에 한 페이지씩 읽어온다.
        //Pageable 객체를 매개변수로 받아서(메소드 호출 시, 어떤 페이지를 보여줄지에 대한 정보를 Pageable 객체에 담아서 전달하는뜻) 페이지 정보를 설정할 수 있다.


        Page<Post> postsPage = postRepository.findAll(pageable);
        //postRepository를 사용하여 데이터베이스에 모든 게시물을 읽어온다. 그리고 Page 객체에 저장. (Pageable 객체에 설정된 페이지 정보를 읽어옴)

        return postsPage.map(post -> new ReadPostResponse(post.getPostId(), post.getTitle(), post.getContent()));
        //읽어온 게시물을 ReadPostResponse 객체로 변환 (게시물의 id, 제목, 내용을 포함). 이것들을 모두 포함한 페이지를 반환.
        //"return postsPage.map(post ->" postsPage에 저장된 각 게시물에 대해 작업을 수행. "map" 메서드는 각각 요서에 대해 특정 작업을 수행, 그 결과를 새로운 스트림으로 반환.
        // 이런 경우, postsPage에 저장된 각 게시물을 ReadPostResponse 객체로 변환하고
        // 여기서 (post -> new ReadPostResponse(post.getPostId(), post.getTitle(), post.getContent()));  각 게시물을 나타내는 변수인 'post'를 받아서 새로운 ReadPostResponse 객체는 생성하는 작업을 수행.(게시물의 id, 제목, 내용이 전달)
        //요약: 각 게시물을 읽어와서, 그 정보를 이용해 새로운 ReadPostResponse 객체를 만들고, 새로운 스트림으로 반환. 이 과정을 통해 각 게시물을 ReadPostResponse형식으로 변환하여 반환하게 된다.

    }
}   //전체요약: 페이지네이션된 형태로 모든 게시물을 읽어오고, 각각의 게시물을 ReadPostResponse 객체로 변환=페이지 단위로 반환하는 역할.
    //"Pagination" 한 번에 많은 양의 데이터를 처리할때 유용-> 데이터를 조각내어 효율적으로 처리할 수 있음.


