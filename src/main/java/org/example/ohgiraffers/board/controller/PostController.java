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
 * REST는 기본적으로 웹의 기준 기술과 HTTP 프로토콜을 그대로 사용하기 때문에,
 * 웹의 장점을 최대한 활용 할 수 있는 아키텍쳐 스타일이다.
 */

@Tag(name = "posts", description = "게시글 API")
@Controller
@RestController
// @RequestMapping: 특정 URL을 매핑하게 도와준다.
@RequestMapping("/api/v1/posts")
// @RequiredArgsConstructor : final 혹은 @NonNull 어노테이션이 붙은 필드에 대한 생성자를 자동으로 생성해준다
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    @Operation(summary = "✏✏✏게시글 작성✒✒✒", description = "제목(title), 내용(content) 입력하기")
    public ResponseEntity<CreatePostResponse> postCreate(@RequestBody CreatePostRequest request) {

        CreatePostResponse response = postService.createPost(request);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{postID}")
    @Operation(summary = "▶▶▶게시글 불러오기◀◀◀", description = "제목(title), 내용(content) 보기")
    public ResponseEntity<ReadPostResponse> postRead(@PathVariable Long postID) {

        ReadPostResponse response = postService.readPostById(postID);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{postId}")
    @Operation(summary = "🛠🛠🛠게시글 수정🛠🛠🛠", description = "제목(title), 내용(content) 수정하기")
    public ResponseEntity<UpdatePostResponse> postUpdate(@PathVariable Long postId, @RequestBody UpdatePostRequest request) {

        UpdatePostResponse response = postService.updatePost(postId, request);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{postId}")
    @Operation(summary = "❌❌❌게시글 삭제❌❌❌", description = "제목(title), 내용(content) 삭제하기")
    public ResponseEntity<DeletePostResponse> postDelete(@PathVariable long postId) {

        DeletePostResponse response = postService.deletePost(postId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "🔍🔍🔍게시글 페이지 조회👀👀👀", description = "페이지별 제목(title), 내용(content) 조회하기")
    public ResponseEntity<Page<ReadPostResponse>> postReadAll(
            @PageableDefault(size = 5, sort = "postId", direction = Sort.Direction.DESC) Pageable pageable) {

       Page<ReadPostResponse> responses = postService.readAllPost(pageable);

       return new ResponseEntity<>(responses, HttpStatus.OK);


    }

}
