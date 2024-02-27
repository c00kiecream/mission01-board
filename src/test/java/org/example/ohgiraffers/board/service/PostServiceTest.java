package org.example.ohgiraffers.board.service;


import jakarta.persistence.EntityNotFoundException;
import org.example.ohgiraffers.board.domain.dto.*;
import org.example.ohgiraffers.board.domain.entity.Post;
import org.example.ohgiraffers.board.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

//Mockito는 자바에서  유닛 테스트를 작성할 때 Mock객체를 생성하고 관리하는 오픈 소스 테스팅 프레임워크.
// + 사용하면 테스트 중에 실제 객체 대신 가상 객체를 사용하여 의존성을 대체.
// + 사용시 밑에 작업들이 가능:
//1. Mock 객체 생성 2. 행동 지정: Mock 객체의 메서드 호출에 대한 행동을 지정. 예_ 특정 메서드가 호출될 때 Mock 객체가 어떤 값을 반환하거나 예외를 발생시킬 수 있다.
//3. 검증: Mockito를 사용하여 Mock 객체의 특정 메서드 호출이 발생했는지 여부를 검증 할 수  있다. 예) 특정 메서드가 호출된 횟수를 확인가능.
@ExtendWith(MockitoExtension.class)
//MockitoExtension을 사용하여 Mockito와 관련된 설정을 자동으로 처리, Mockito의 Mock 객체를 초기화. 이를 통해 Mockito를 사용하여 테스트에 Mock 객체를 만들고 관리하는데 필요한 설정을 가능하게함.
public class PostServiceTest {

    /** @Mock
     * 가짜 객체, 테스트 실행 시 실제가 아닌 Mock 객체를 반환한다.
     */
    @Mock
    private PostRepository postRepository;  //PostRepository인터페이스를 구현한 Mock객체를 생성하는 것을 나타냄.

    /** @InjectMocks //(Mockito)에서 사용, 테스트 대상 클래스의 필드에 자동으로 Mock객체가 생성/주입.
     * Mock 객체가 주입될 클래스를 지정한다.
     */
    @InjectMocks    //아래 필드들은 테스트에서 사용될 각종 객체나 데이터를 표현, @InjectMocks로 주입되는 PostService와 함께 테스트에서 활용된다.
    private PostService postService; //여기서 PostService는 테스트 대상인 클래스. 테스트에서 필요한 객체를 쉽게 사용할 수 있음.

    private Post savedPost; //저장된 게시물을 나타내는 객체

    private Post post;  //게시물을 나타내는 객체

    private CreatePostRequest createPostRequest;    //게시물 생성 요청을 나타내는 객체

    private UpdatePostRequest updatePostRequest;    //게시물 수정 요청을 나타내는 객체.

    @BeforeEach //각각의 테스트가 실행되기 전에 호출된다. 테스트 메서드에서 사용할 객체들을 초기화하고 설정.
    void setup() {
        //초기화
        post = new Post(1L, "테스트 제목", "테스트 내용");    //테스트용 게시물 객체로 Id가 1인 테스트 게시물을 나타낸다.
        savedPost = new Post(2L, "저장되어 있던 테스트 제목", "저장되어 있던 테스트 내용");   //저장되어 있는 게시물 객체, Id가 2인 테스트 게시물을 나타낸다.
        createPostRequest = new CreatePostRequest("테스트 제목", "테스트 내용");  //게시물 생성 요청을 나타내는 객체, 테스트제목/테스트내용을 가진 요청을 나타낸다.
        updatePostRequest = new UpdatePostRequest("변경된 테스트 제목", "변경된 테스트 내용");  //게시물 수정 요청을 나타내는 객체, 변경된테스트제목/변경된테스트내용을 가진 요청을 나타낸다.
    }

    @Test
    @DisplayName("게시글 작성 기능 테스트")
    void create_post_test() {
        //given

        //mockito 기본 형태
        //when(postRepository.save(any())).thenReturn(post);

        //BDDMockito 형태로
        given(postRepository.save(any())).willReturn(post);
        //설명: postRepository.save(any())메서드가 호출되면 어떤 값이 전달되든 (any())post객체를 반환하도록 설정.
        // 즉, 실제로 데이터베이스에 저장되지 않고, 가상의 데이터인 post객체를 반환하도록 설정한다는 것을 의미.
        //요약: 테스트를 위해 가짜 데이터를 반환하도록 설정. 따라서 실제 데이터베이스에 데이터를 저장하지 않고 원하는 결과를 얻을 수 있다.

        //when
        CreatePostResponse createPostResponse = postService.createPost(createPostRequest);
        //설명: postService.createPost(createPostRequest)를 호출, 게시글을 생성하고 그 결과를 createPostResponse변수에 저장.
        //(가상의 게시글 생성 요청 객체를 이용하여 게시글을 생성하는 메서드를 호출, 그 결과를 createPostResponse에 저장.

        //then
        //assertThat: 테스트에서 원하는 조건을 검증하는데 사용되는 메서드.
        assertThat(createPostResponse.getPostId()).isEqualTo(1L);   //생성된 게시글의 id가 1인지 검사
        assertThat(createPostResponse.getTitle()).isEqualTo("테스트 제목");  //생성된 게시글의 제목이 테스트제목인지 검사
        assertThat(createPostResponse.getContent()).isEqualTo("테스트 내용");    //생성된 게시글의 내용이 테스트내용인지 검사.
        //요약: 테스트는 createPostRequest를 이용하여 게시글을 생성, 그 결과가 기대한 값과 일치하는지 확인 및 검증.
    }

    @Test
    @DisplayName("postId로 게시글을 조회하는 기능 테스트")
    void read_post_by_id_1() {      //메서드: read_post_by_id_1, 아무런 매개변수를 받지않으며(void) 어떤 값도 반환하지 않는다. 특정 postId로 게시글을 조회하고 그 결과를 확인.;
        //given
        when(postRepository.findById(any())).thenReturn(Optional.of(savedPost));
        //저장된 게시글이 있다고 설정, postRepository.findById(any())는 어떤 postId가 들어와도 저장된 게시글을 반환한다는 뜻. 따라서 thenReturn(Optional,.of(savedPost))는 저장된 게시글을 반환하도록 설정한 것.

        //when
        ReadPostResponse readPostResponse = postService.readPostById(savedPost.getPostId());
        //설명: postService.readPostById(savedPost.getPostId())를 호출, 저장된 게시글을 특정 postId로 조회, 여기서 savedPost.getPostId는 가정한 저장된 게시글의 postid를 사용하여 조회를 시도.

        //then          (조회된 게시글의 정보가 예상한 대로인지 확인)
        assertThat(readPostResponse.getPostId()).isEqualTo(savedPost.getPostId());
        assertThat(readPostResponse.getTitle()).isEqualTo(savedPost.getTitle());
        assertThat(readPostResponse.getContent()).isEqualTo(savedPost.getContent());
        //assertThat을 사용하여 조회된 게시글의 ID, 제목, 내용이 저장된 게시글과 동일한지 검증.
        //요약: 이 테스트는 postId를 사용하여 게시글을 조회, 그 결과를 검증하는 과정.
    }

    @Test
    @DisplayName("postId로 게시물을 찾지 못했을 때, 지정한 Exception을 발생시키는지 테스트")
    void read_post_by_id_2() {  //메서드, postid로 게시물을 찾지 못했을 때 예외를 발생시키는 테스트.
        //given
        given(postRepository.findById(any())).willReturn(Optional.empty());
        //설명: Mockito의 given 메서드는 목 객체가 특정 메서드 호출에 대해 어떻게 동작해야 하는지 설정하는 역할.
        // postRepository.findById(any()): postRepository객체의 findById메서드가 호출되는 경우. any()는 어떤 매개변수 값이든 가능하다는 것을 의미.
        //findById메서드가 호출될 떄 값이 없다면, 이를 나타내기 위해 Optional.empty()를 반환하도록 설정. 게시물이 없는 상황을 가상적으로 시뮬레이션하기 위해 사용된 목 객체 설정.
        //요약: 만약 postRepository 에서 findById 메서드가 호출되면, 어떤 매개변수 값이든 상관없이 비어있는 상태의 Optional 객체를 반환하도록 해줘라고 요청하는 부분이다.

        //when & then
        assertThrows(EntityNotFoundException.class, () ->   //readPostById 메서드가 호출될 때 예외가 발생하는지 테스트.
                postService.readPostById(1L));  //EntityNotFoundException: 데이터베이스나, 다른 저장소에 엔티티를 찾을 수 없는 경우에 발생. 예 ) 해당 ID로 찾으려는 엔티티가 존재하지않을 떄 이 예외가 발생합니다.
        //assertThrows: 메서드: 특정 예외가 발생하는지를 테스트 하는데 사용. 예상하지 못한 예외가 발생하는지 테스트 할수 있다.
    }

    @Test
    @DisplayName("전체 게시글 조회 기능 테스트")
    void read_all_post() {

        //given
        //설명: Pageable 객체를 생성하여 페이지 번호와 페이지 크기를 설정(한 페이지의 5개의 항목을 표시하도록 요청을 생성)
        Pageable pageable = PageRequest.of(0, 5);
        List<Post> posts = Arrays.asList(post, savedPost);  //생성된 게시글을 리스트에 담는다.
        Page<Post> postsPage = new PageImpl<>(posts, pageable, posts.size());   //리스트를 페이지로 변환. PageImpl<>(posts, pageable, posts.size()): 위에서 생성한 페이지 요청과 게시글 리스트를 사용하여 페이지 객체를 만든다.

        given(postRepository.findAll(pageable)).willReturn(postsPage);
        //Repository 메서드 설정: 게시글을 조회하는 Repository의 findAll 메서드가 주어진 페이지 요청을 받았을 때, 위에서 만든 페이지를 반환하도록 설정.

        //when
        Page<ReadPostResponse> responses = postService.readAllPost(pageable);
        //설명: postService.readAllPost(pageable)는 서비스 클래스에서 전체 게시글을 페이지로 조회하는 메서드를 호출하는 부분.
        //+ 페이지정보를 포함한 Pageable 객체를 매개변수로 가지고 있는 readAllPost 메서드를 호출. 주어진 페이지 정보를 기반으로 게시글을 조회, 조회된 결과를 페이지로 반환.
        //+ 조회된 게시글 페이지는 Page<ReadPostResponse>타입의 변수인 responses에 저장.
        //테스트 중 서비스의 readAllPost메서드가 호출되어 전체 게시글을 페이지로 정상적으로 조회하는지 확인하는 과정.


        //then      (테스트에서 기대하는 결과를 확인하는 단계)
        assertThat(responses.getContent()).hasSize(2);  //조회된 목록의 크기가 2인지 확인. 두 개의 게시글이 조회되어야 한다.
        assertThat(responses.getContent().get(0).getTitle()).isEqualTo("테스트 제목");
        assertThat(responses.getContent().get(0).getContent()).isEqualTo("테스트 내용");
        //첫 번째 게시글의 제목과 내용이 각각 테스트제목/테스트내용과 일치하는지 확인.
        assertThat(responses.getContent().get(1).getTitle()).isEqualTo("저장되어 있던 테스트 제목");
        assertThat(responses.getContent().get(1).getContent()).isEqualTo("저장되어 있던 테스트 내용");
        //두 번째 게시글의 제목과 내용이 각각 저장되어있던테스트제목/내용과 일치하는지 확인.
    }   //asssertThat(메서드): 특정 조건이 참인지 확인하는데 사용.

    @Test
    @DisplayName("게시글 삭제 테스트")
    void delete_post() {

        //given
        when(postRepository.findById(any())).thenReturn(Optional.of(savedPost));

        //when
        DeletePostResponse deletePostResponse = postService.deletePost(savedPost.getPostId());

        //then
        assertThat(deletePostResponse.getPostId()).isEqualTo(2L);

    }

//    @Test
//    @DisplayName("postId로 게시물을 찾지 못했을 때, 지정한 Exception을 발생시키는지 테스트")
//    void delete_post_by_id_2() {
//        //given
//        given(postRepository.findById(any())).willReturn(Optional.empty());
//
//        //when & then
//        assertThrows(EntityNotFoundException.class, () ->
//                postService.deletePost(1L));
//    }

    @Test
    @DisplayName("게시글 수정 기능 테스트")
    void update_post_test() {

        given(postRepository.findById(any())).willReturn(Optional.of(savedPost));

        //when
        UpdatePostResponse updatePostResponse = postService.updatePost(savedPost.getPostId(), updatePostRequest);

        //then
        assertThat(updatePostResponse.getPostId()).isEqualTo(savedPost.getPostId());
        assertThat(updatePostResponse.getTitle()).isEqualTo("변경된 테스트 제목");
        assertThat(updatePostResponse.getContent()).isEqualTo("변경된 테스트 내용");


    }
}
