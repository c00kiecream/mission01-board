package org.example.ohgiraffers.board.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.ohgiraffers.board.domain.dto.*;
import org.example.ohgiraffers.board.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/** 레이어드 아키텍쳐
 * 소프트웨어를 여러개의 계층으로 분리해서 설계하는 방법
 * 각 계층이 독립적으로 구성되서, 한 계층이 변경이 일어나도, 다른 계층에 영향을 주지 않는다.
 * 따라서 코드의 재사용성과 유지보수성을 높일 수 있다.
 */

/** Controller RestController
 * Controller 주로 화면 View를 반환 하기 위해 사용된다.
 * 하지만 종종 Controller를 쓰면서도 데이터를 반환 해야 할 때가 있는데, 이럴 때 사용하는 것이 @ResponseBody이다.
 *
 * REST란?
 * Representational AState Transfer의 약자
 * 자원을 이름으로 구분하여 자원의 상태를 주고 받는 것을 의미한다.
 * 데이터의 표현을 주고받으며, 클라이언트와 서버간의 상태 전이를 통해 상호작용한다.
 * REST는 기본적으로 웹의 기준 기술과 HTTP 프로토콜을 그대로 사용하기 때문에,
 * 웹의 장점을 최대한 활용 할 수 있는 아키텍쳐 스타일이다.
 * 특징:
 * 리소스 식별: 각 리소스는 고유한 식별자를 가지며, 이를 통해 클라이언트가 리소스에 접근.
 * 예) /users는 사용자 정보를 나타내는 리소스
 * 표현: 리소스는 JSON, XML 형식으로 데이터를 표현.
 * 상태 없음: 서버는 클라이언트의 상태를 관리하지않는다. 각 요청은 모든 필요한 정보를 포함하고 있어야하며, 이를 통해 서버는 클라이언트와 독립적으로 동작할 수 있음.
 * 인터페이스 일관성: 일관된 인터페이스를 제공하여 서비스의 유연성과 확장성을 높인다. HTTP메서드 (GET POST PUT DELETE)를 사용하여 리소스에 대한 작업을 수행.
 * 계층 구조: 클라이언트는 서버와 직접 통신하는것이 아니다. 서버 중간에 다른 서버나 캐시등을 둘 수 있다. 이를 통해 시스템의 확장성과 보안성을 높일 수 있다.
 */

@Tag(name = "posts", description = "게시글 API")
@Controller
@RestController
// @RequestMapping: 특정 URL을 매핑하게 도와준다.
@RequestMapping("/api/v1/posts")
// @RequiredArgsConstructor : final 혹은 @NonNull 어노테이션이 붙은 필드에 대한 생성자를 자동으로 생성해준다
@RequiredArgsConstructor
public class PostController {   //게시물과 관련된 http 요청을 처리

    private final PostService postService;  //PostService클래스의 인스턴스를 가리키는 필드, 이필드를 통해 PostService에서 제공하는 기능을 호출

    @PostMapping
    @Operation(summary = "✏✏✏게시글 작성✒✒✒", description = "제목(title), 내용(content) 입력하기")

    //새로운 글을 작성하는 기능을 담담하는 메서드를 보여준다.
    public ResponseEntity<CreatePostResponse> postCreate(@RequestBody CreatePostRequest request) {
//postCreate: 메서드(새로운 글을 작성하는 역할) , @Requestbody CreatePostRequest request: 메서드가 받는 입력 값, "CreatePostRequest: 새로 작성할 글에 대한 정보를 담고 있다. 예)글의 제목/내용
        CreatePostResponse response = postService.createPost(request);
//여기서 postService는 글을 작성하는데 필요한 로직(단계/규칙)이 담긴 서비스를 가리킨다. 이 코드는 서비스에게 새로운 글을 작성하라고 요청하고, 그결과로 생성된 글에 대한 정보를 "response"에 받아옵니다.
        return new ResponseEntity<>(response, HttpStatus.OK);
    } // 새로 작성된 글에 대한 정보를 담은 response를 클라이언트에 돌려준다. 응답의 상태코드 200으로 설정하여 요청이 성공적으로 처리된것을 알려저ㅜㄴ다.
    //요약: 새로운 글을 작성하는 요청을 받고, 요청에 따라 새로운 글을 작성, 작성된 글에 대한 정보를 클라이언트에게 반환하는 역할을 한다.

    @GetMapping("/{postID}")
    @Operation(summary = "▶▶▶게시글 불러오기◀◀◀", description = "제목(title), 내용(content) 보기")

    //특정 글을 읽는 기능을 수행하는 메서드
    public ResponseEntity<ReadPostResponse> postRead(@PathVariable Long postID) {
//postRead: 메서드(특정 글을 읽는 역할), 해당 ID를 가진 글의 정보를 가져온다. , @PathVariable(요청한 URL에서 특정한 값(글의id)를 가져올때 사용) Long postID= 메서드가 받는 입력 값. postID는 url에서 전달되는 글의 고유한 식별자. 사용자가 읽고자하는 글의 id
        ReadPostResponse response = postService.readPostById(postID);
//'postService'는 글을 읽는 데 필요한 로직이 담긴 서비스. 특정 id에 가진 글을 읽어오라는 요청을 보내고, 생성된 글에 대한 정보를 'response'에 받아옴.
        return new ResponseEntity<>(response, HttpStatus.OK);
    } // 메서드: 읽어온 글에 대한 정보를 클라이언트에 반환. 응답 상태 코드를 설정하고 요청이 성공적으로 처리됨을 알려준다.
    //요약: 특정 글을 읽는 요청을 받고, 해당 글의 정보를 가져와서 클라이언트에 반환하는 역할을 한다.

    @PutMapping("/{postId}")
    @Operation(summary = "🛠🛠🛠게시글 수정🛠🛠🛠", description = "제목(title), 내용(content) 수정하기")

    //특정 글을 업데이트하는 기능을 담당하는 메서드.
    public ResponseEntity<UpdatePostResponse> postUpdate(@PathVariable Long postId, @RequestBody UpdatePostRequest request) {
//postUpdate: 메서드(특정 글을 업데이트 하는 역할), @PathVariable Long postId=메서드가 받는 첫 입력 값. postId=사용자가 업데이트하고자 하는 글의 id. "@RequestBody UpdatePostRequest request"=메서드가 받는 두 번째 값. "UpdatePostRequest" 업데이트할 글에 대한 정보를 담고 있다. 예) 새로운 제목이나/내용
        UpdatePostResponse response = postService.updatePost(postId, request);
//postService=글을 업데이트하는 데 필요한 로직이 담긴 서비스. 특정 id를 가진 글을 덥데이트하라는 요청을 보내고, 생성된 업데이트된 글에 대한 정보를 'response'에 받아온다.
        return new ResponseEntity<>(response, HttpStatus.OK);
    } //메서드: 업데이트된 글에 대한 정보를 클라이언트에 반환. 응답코드 설정 후 요청이 성공처리된것을 알림.
    //요약: 특정 글을 업데이트하는 요청을 받고, 해당 글을 업데이트 후 정보를 클라이언트에 반환하는 역할.

    @DeleteMapping("/{postId}")
    @Operation(summary = "❌❌❌게시글 삭제❌❌❌", description = "제목(title), 내용(content) 삭제하기")
    //특정 글을 삭제하는 기능을 수행하는 메서드.
    public ResponseEntity<DeletePostResponse> postDelete(@PathVariable long postId) {
//postDelete : 메서드(특정 글을 삭제하는 역할), @PathVariable Long postId=메서드가 받는 입력 값, url에서 변수로 전달되는 글의 postid를 나타냄. 예)사용자가 삭제하고자 하는 글의 id.
        DeletePostResponse response = postService.deletePost(postId);
//postService=글을 삭제할 때 필요한 로직이 담긴 서비스. 특정 id를 가진 글을 삭제하라는 요청을 보내고, 생성된 삭제된 글에 대한 정보를 response에 받아온다.
        return new ResponseEntity<>(response, HttpStatus.OK);
    }//메서드: 삭제된 글에 대한 정보를 반환. 상태 코드로 요청 알림.
    //요약: 특정 글을 삭제하는 요청을 받고, 해당 글을 삭제, 삭제된 글에 대한 정보를 클라리언트에게 반환하는 역할.

    @GetMapping
    @Operation(summary = "🔍🔍🔍게시글 페이지 조회👀👀👀", description = "페이지별 제목(title), 내용(content) 조회하기")

    //모든 글을 읽어오는 역할, postReadAll: 메서드(모든 글을 읽어오는 역할).
    public ResponseEntity<Page<ReadPostResponse>> postReadAll(
            @PageableDefault(size = 5, sort = "postId", direction = Sort.Direction.DESC) Pageable pageable) {
//요청한 글 목록을 페이지로 나누고, 정렬하는 기능을 담당: size=5[한 페이지에 5개의 글을 표시] sort=postId[정렬기준:postid를 기준으로 정렬] direction = Sort.Direction.DESC[정렬방향, 내림차순:최근 작성된 글이 먼저 표시)
        //요약: 글 목록 요청할 때, 한페이지에 5개의 글을 표시, postId를 기준으로 내림차순으로 정렬.
       Page<ReadPostResponse> responses = postService.readAllPost(pageable);
//postService:모든 글을 읽어오는데 필요한 로직이 담긴 서비스. 요청된 페이지 정보에 따라 모든 글을 읽어오라는 요청을 보내고, 생성된 페이지에 포함된 글 목록에 대한 정보를 'response'에 받아옴
       return new ResponseEntity<>(responses, HttpStatus.OK);
//메서드: 읽어온 모든 글에 대한 페이지를 반환. 상태 코드로 요청 알림.
        //요약: 모든 글을 읽어오는 요청을 받고, 요청된 페이지에 포함된 모든 글 목록을 클라이언트에 반환하는 역할. 페이지네이션 및 정렬 기능을 제공. 

    }

}
