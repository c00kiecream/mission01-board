package org.example.ohgiraffers.board.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.ohgiraffers.board.domain.dto.*;
import org.example.ohgiraffers.board.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.sql.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/** 통합테스트 & 단위테스트
 * 통합테스트:
 * 모듈을 통합하는 과정에서 모듈 간의 호환성을 확인하기 위해 수행되는 테스트
 * 통합테스트는 API의 모든 과정이 올바르게 동작하는지 확인하는 것
 *
 * 단위(유닛) 테스트:
 * 하나의 모듈을 기준으로 독립적으로 진행되는 가장 작은 단위의 테스트(작은 단위 = 함수, 메소드)
 * 단위테스트는 어플리케이션을 구성하는 하나의 기능이 올바르게 동작하는지 독립적으로 테스트하는 것
 */

/** @WebMvcTest
 * MVC(Model,View,Controller)를 위한 테스트, 컨트롤러가 예상대로 동작하는지 테스트하는데 사용된다.
 * 웹 어플리케이션을 어플리케이션 서버에 배포하지 않고 테스트용 MVC환경을 만들어 요청 및 전송 응답기능을 제공해준다.
 */
@WebMvcTest(PostController.class)
//특정 컨트롤러를 테스트할 때 사용. 예)PostController
//PostController에 정의된 엔드포인트들이 요청을 올바르게 처리하고 응당하는 지 확인.
//예) 게시물을 생성하거나 조회하는 메서드가 올바르게 동작하는지 확인.
//웹 애플리케이션에서 사용되는 중요한 구성 요소
public class PostControllerTest {

    /** @Autowired
     * 의존성 주입(DI)을 할 때, 사용하는 필요한 의존 객체의 타입이 해당하는 빈을 찾아 주입한다.
     * 의존성 : 하나의 코드가 다른 코드에 의존하는 상태를 뜻한다.
     *
     */
    @Autowired //스프링에서 객체를 주입할 떄 사용. 이 경우에는 MockMvc 객체를 주입받고 있고 해당 컨트롤러를 테스트할 때 MockMvc 객체를 사용
    MockMvc mockMvc; //테스트용 가짜 웹 환경.
    //실제 웹 서버를 실행하지 않고도 컨트롤러 테스트 하게 가능. MockMvc를 사용하면 HTTP요청을 보내고 컨트롤러의 응답을 확인 할 수 있음.
    //예) 특정 url에 대한 GET 요청을 보내고 응답 상태코드나 응답 본문을 검증.
    //요약: MockMvc 객체를 주입받고 해당 컨트롤러를 테스트할 때 사용 가능하게 함. 이를 통해 컨트롤러의 동작을 검증하고 테스트할 수 있음.

    //@Bean: 메서드에 사용
    // 데이터베이스 연결을 위한 DataSource객체를 생성하고 싶다면 @Bean을 사용하여 해당 객체를 생성하는 메서드에 지정. 그 메서드가 만드는 객체를 스프링이 알아서 관리하게 된다. 스프링이 필요할 때 마다 해당 객체를 가져다가 사용할 수 있게 된다.
    //@Bean을 사용하면 "이 메서드가 만드는 객체를 나중에 쓸 거야!"라고 알려줌
    /** @MockBean
     * 스프링의 의존성 주입기능을 사용해 테스트 실행 시 실제 빈 대신에 Mockito로 생성됨
     * 모의객체를 스프링 어플리케이션 컨텍스트에 추가한다.
     */
    //실제 빈과 같은 역할을 하지만 가상 객체.
    //이것을 사용하면 다른 빈과의 상호 작용을 테스트 함. 예) 외부 서비스와 통신하는 빈을 테스트할 떄 해당 외부 서비스 대신 가짜 객체를 사용하여 테스트를 진행.
    @MockBean   //실제 객체처럼 동작하지 않지만 테스트에서 필요한 동작을 지정
    PostService postService;    //서비스: 만들어 놓은 비즈니스 로직을 담고 있음 (게시물 저장하거나 읽어오는 기능)
//요약: 테스트를 할 때 사용할 가짜 PostService 객체를 만들어 postService 변수에 넣는 것. 이렇게하면 직접 서비스 객체를 만들 필요 없이, 가짜 객체를 사용하여 테스트 가능.
    /** ObjectMapper:
     * 특정 객체를 json 형태로 바꾸기위해 사용한다.
     */
    @Autowired  //스프링 도구: ObjectMapper를 자동으로 찾아서 넣어준다. 알아서 필요한 도구를 찾아서 사용 가능하게함. ObjectMapper를 따로 만들거나 설정할 필요X
    ObjectMapper objectMapper;  //JSON 데이터와 Java 객체를 서로 변환할 떄 사용되는 도구.
    //예) JSON 형식의 데이터를 받았을 때, Java 객체로 바꾸거나, Java 객체를 Json으로 바꿀 떄 사용
    //요약: Json과 Java 객체를 변환하는데 사용되는 ObjectMapper 도구를 스프링이 자동으로 넣어준다.

    @Test
    @DisplayName("게시글 작성 기능 테스트")   //테스트 이름을 지정
    void create_post_test() throws Exception { //메서드의 선언 부분. 메서드 이름: create_post_test이고, 아무런 값도 반환하지 않는다는 것을 나타냄.
        //예외 처리하기 위해 throws Exception이 추가됨. 테스트 메서드의 시작을 나타냄.


        //given+when+then: BDD(Behaviour-Driven Development)테스트에서 사용되는 주석. 코드 블록의 역할을 알려준다.

        //given: 테스트 준비 단계-테스트에서 사용할 가짜 객체를 생성하거나 테스트 데이터를 초기화하는 작업을 수행.
        CreatePostRequest request = new CreatePostRequest("테스트 제목", "테스트 내용");
        //테스트 제목과 테스트 내용을 가진 새로운 게시글 생성 요청 객체를 만든다. (이 요청 객체는 게시글을 생성하는데 필요한 데이터를 담고 있다)
        CreatePostResponse response = new CreatePostResponse(1L, "테스트 제목", "테스트 내용");
        //여기서는 게시글이 성공적으로 생성되었을 때 응답을 나타내는 객체를 생성.(여기서는 생성된 게시글의 id가 1, 제목:테스트제목, 내용:테스트내용인 응답 객체를 생성.
        given(postService.createPost(any())).willReturn(response);
        //postService.createPost() 메서드가 호출되면, any()메서드로 인해 어떤 입력값이 들어와도 상관없이 설정된 response 객체가 반환.
        //+ 이렇게 했을때 실제로 데이터베이스에 게시글이 생성되는 과정은 실행이 안되는 대신 우리가 지정한 응답값을 받게 된다.
        // + 따라서 테스트를 실행할 때 실제로 게시글을 생성안해도, postService.createPost() 메서드의 동작을 테스트 할 수 있다.




        //when(테스트에서 특정 동작을 수행하는 부분;테스트에서 기능을 실행하거나 메서드를 호출하는 작업을 수행 & then(테스트 결과를 검증하는 부분; 부분이 충족되는지 확인하고 예상한 결과가 실제로 발생하는지 검사하는 작업.
        //요약: 테스트 코드의 구조를 명확하게 파악하고, 의도를 더 쉽게 이해할 수 있음. 테스트코드의 가독성과 유지보수성을 높이는 데 도움.
        mockMvc.perform(post("/api/v1/posts")   //가상으로 서버에 Post요청을 보내고 이때 요청의 내용은 request 객체에 담긴 정보를 json 형태로 반환하여 전송.
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request))
        )
//JSON: JavaScript Object Notation=데이터를 저장하고 전송하기 위한 데이터 형식. 일반적으로 키/값 쌍으로 이루어진 데이터를 표현.
//jsonPath: JSON 응답의 특정 경로에 있는 값을 검증하는 메서드. (게스글 정보를 담은 JSON이 있다면, 그안에는 제목, 내용과 같은 정보가 있다.jsonPath는 이런 JSON데이터의 특정 부분을 찾아내는 것이다.
                //예) .andExpect(jsonPath("$.content").value("테스트 내용")) 에서 $는 응답의 최상위 노드. ".content"는 해당 응답의 content 필드를 나타낸다. 즉, 이 코드는 서버의 응답에서 content 필드의 값이 테스트 내용과 일치하는지를 확인한다.

                .andExpect(status().isOk()) //서버가 요청을 처리한 결과고 http상태 코드가 200(ok)인지 검증.
                .andExpect(jsonPath("$.postId").value(1L))
                .andExpect(jsonPath("$.title").value("테스트 제목"))
                .andExpect(jsonPath("$.content").value("테스트 내용"))
                //요청에 대한 응답이 맞는지 검증. $.postId, $.title, $.content는 응답 json의 각 필드를 나타냄.
                //1L, "테스트제목","테스트내용"은 각각 응답 Json의 postId, title, content필드에 대한 기대값을 나타냄.
                .andDo(print());
                //테스트를 수행할 때 추가적인 정보를 콘솔에 출력하도록 설정하는 부분.
        //요약: 가상으로 서버에 post요청을 보내고, 서버가 이를 처리한 후에 올바른 응답을 받았는지 검증하는 것.
    }

    @Test
    @DisplayName("게시글을 단일 조회하는 테스트")
    void read_post_test() throws Exception {        //메서드: read_post_test

        //given
        Long postID = 1L;   //변수에는 조회할 게시글의 id를 지정. 여기서는 1L
        ReadPostResponse response = new ReadPostResponse(1L, "테스트 제목", "테스트 내용");
        //response객체에는 테스트에서 반환할 게시글의 정보가 들어가 있다. 이 경우 게시글의 id가 1L, 제목과 내용이 "테스트제목"/"테스트내용"인 게시글 정보가 들어가 있음.

        given(postService.readPostById(any())).willReturn(response);    //postService 메서드가 호출되면 어떤 값이 전달되든 준비한 response 객체를 반환하도록 설정.

        //when & then
        mockMvc.perform(get("/api/v1/posts/{postId}", postID))  //가상으로 GET요청을 보내고, 여기서 {postid}는 위에서 설정한 postID 변수의 값인 1L로 대체. 즉, id가 1인 게시글을 조회하는 요청을 보내는것.
                .andExpect(status().isOk())    //서버가 요청을 제대로 처리했는지 확인 (결과로 http 상태 코드가 OK인지를 검증)
                .andExpect(jsonPath("$.postId").value(1L))
                .andExpect(jsonPath("$.title").value("테스트 제목"))
                .andExpect(jsonPath("$.content").value("테스트 내용"))
                //요청에 대한 응답이 올바른지 검증. $.postid, $.title, $.content는 응답 json의 각 필드를 나타내고, 이 값들이 각각 1L, 테스트제목, 테스트 내용과 일치하는지 확인.
                .andDo(print());

    }

    @Test
    @DisplayName("게시글을 수정하는 테스트")
    void update_post_test() throws Exception {  //테스트 메서드(update_post_test) 의 선언. 이 메서드는 어떤 값도 반환하지 않는다. 또한, void throw Exception 은 메서드에서 발생할 수 있는 예외 처리하기 위한 것.

        //given
        Long postId = 1L;   //postId라는 변수에 1L이라는 값을 할당.(테스트하는 게시글의 ID를 나타낸다.)

        UpdatePostRequest request = new UpdatePostRequest("변경 제목", "변경 내용");    //UpdatePostRequest 객체를 생성하고, 이 객체는 게시글을 수정하는 요청을 나타내며 제목은 변경제목, 내용은 변경내용 이다.
        UpdatePostResponse response = new UpdatePostResponse(1L, "변경 제목", "변경 내용"); //UpdatePostResponse 객체를 생성하고, 이 객체는 게시글 수정에 대한 응답을 나타내며, 게시글의 Id는 1L이고, 제목과 내용은 각각 변경제목과 변경 내용이다.

        given(postService.updatePost(any(Long.class), any(UpdatePostRequest.class))).willReturn(response);
        //설명: postService.updatePost()메서드가 호출될 떄 어떤 입력값이 들어와도 우리가 설정한 (any)response 객체를 반환하도록 하는 것.
        // 가상의 데이터를 설정하고 특정 메서드가 어떤 입력값을 받든 항상 동일한 결과를 반환하도록 만드는 것.

        //when & then
        mockMvc.perform(put("/api/v1/posts/{postId}", postId)   //가상의 HTTP PUT 요청을 만들어 테스트. 요청 URL은 "/api/v1/posts/{postId}", {postID}부분은 위에서 설정한 postID변수의 값인 1L로 대체.
                        .contentType(MediaType.APPLICATION_JSON)    //요청의 타입을 JSON으로 설정
                        .content(objectMapper.writeValueAsBytes(request))
                        //요청의 본문에는 request 객체를 JSON으로 변환한 값을 넣는다.
                        //예를 들어, request 객체에 제목과 내용이 각각 "테스트제목"/"테스트내용"으로 설정되었다면 이코드는 요청의 본문에 {"title":"테스트 제목","content":"테스트 내용"}와 같은 형태의 JSON 데이터가 들어간다.
                )

                .andExpect(status().isOk()) //서버가 요청을 처리한 결과고 http상태 코드가 200(ok)인지 검증.
                .andExpect(jsonPath("$.postId").value(1L))  //서버가 반환한 JSON응답에서 postID필드의 값이 1인지 확인. 게시글의 ID가 1로 설정된것인지 검증.
                .andExpect(jsonPath("$.title").value("변경 제목"))  //서버가 반환한 JSON응답에서 title 필드의 값이 변경제목인지 확인. 수정된 게시글의 제목이 변경제목으로 설정된것인지 검증.
                .andExpect(jsonPath("$.content").value("변경 내용"))    //서버가 반환한 JSON응답에서 content 필드의 값이 변경내용인지 확인. 수정된 게시글의 내용이 변경 내용으로 설정된것인지 검증.
                //요청에 대한 응답이 맞는지 검증. $.postId, $.title, $.content는 응답 json의 각 필드를 나타냄.
                //1L, "테스트제목","테스트내용"은 각각 응답 Json의 postId, title, content필드에 대한 기대값을 나타냄.
                .andDo(print());    //테스트 실행 중 추가적인 정보를 출력하는 역할. 테스트 실패시 서버의 응답이나 다른 정보를 확인할 떄 유용하고 콘솔에 출력해서 확인 가능.
    }

    @Test
    @DisplayName("게시글을 삭제하는 테스트")
    void delete_post_test() throws Exception {  //테스트 메서드임을 나타내며 아무것도 반환하지 않고 Exception을 던질수 있음을 나타냄.

        //given
        Long postID = 1L;   //테스트를 위한 가상의 게시물 ID를 설정
        DeletePostResponse deletePostResponse = new DeletePostResponse(postID); // 가상의 DeletePostResponse 객체를 생성하고, 가상의 게시물 ID를 사용하여 초기화한다.

        given(postService.deletePost(any(Long.class))).willReturn(deletePostResponse);
        //postService의 deletePost()메서드가 호출될 때, 어떤 값이 전달되든 상관없이 any(Long.class), 특정한 응답(deletePostResponse)를 반환하도록 설정.
        // 여기서 deletePostResponse는 가상의 삭제 응답 객체를 나타낸다.

        //when & then   //실제로 테스트 동작을 수행하고 예상된 결과를 확인.
        mockMvc.perform(delete("/api/v1/posts/{postId}", postID))   //mockMVC를 사용하여 지정된 /api/v1/posts/{postId}로 delete 요청을 수행. 이때 가상의 게시물 ID를 사용.
                .andExpect(status().isOk()) //서버가 요청을 처리한를 HTTP응답 상태로 확인. OK로 코드가 출력시 성공적으로 처리됨.
                .andExpect(jsonPath("$.postId").value(1L))  //JSON응답 본문에 postID필드의 값이 1L인지 확인. 요청을 보낸 후 응답에서 postID라는 필드가 있는지, 그리고 그 값이 1로 설정된것인지 검증하는 역할.
                .andDo(print());
        //요약: 이 테스트는 특정ID(post ID)를 가진 게시물을 삭제하는 DELETE 요청을 수행한다. 그리고 요청이 성공적으로 처리되고, JSON 응답 본문에 예상한 게시물ID가 포함되어있는 확인.

   }

   @Test
   @DisplayName("모든 게시글 페이지 단위로 조회하는 기능 테스트")
   void read_all_post_test() throws Exception { //테스트 메서드(read_all_post_test)를 나타내며, 아무것도 반환하지 않고 Exception을 던질 수 있음을 나타낸다.

        //given
       int page = 0;    //테스트를 위한 페이지 번호를 설정
       int size = 5;    //페이지 크기(한 페이지에 포함될 항목의 개수)
       PageRequest pageRequest = PageRequest.of(page,size); //페이지 요청 객체를 생성하는 메서드. 위에서 설정한 페이지 번호와 페이지 크기를 0,5의 값으로 넘겨주어 페이지 요청 객체를 생성. 
       ReadPostResponse readPostResponse = new ReadPostResponse(1L, "테스트 제목", "테스트 내용");    //테스트에 사용할 게시글의 응답 객체를 생성, 이 경우 Id=1, 제목/내용=테스트제목/테스트내용인 응답 객체를 만들었다.
       List<ReadPostResponse> responses = new ArrayList<>();
       // 'responses'라는 이름의 리스트를 생성. 이 리스트에 게시글 응답 객체를 담는다. 여기서 ArrayList<>는 구현체(배열 기반으로 요소를 저장하는 동작 배열-이것을 사용하여 리스트를 생성하고 요소를 추가,제거,검색 작업을 수행).
       // 리스트는 여러 개의 항목을 담을 수 있는 자료구조이다.
       responses.add(readPostResponse); //위에서 만든 게시글 응답 객체를 리스트에 추가:하나의 게시글을 포함한 응답 리스트를 만들었다.

       Page<ReadPostResponse> pageResponses = new PageImpl<>(responses, pageRequest, responses.size());
        //페이지 응답 객체를 생성. PageImpl은 SpringData에서 제공하는 페이징 처리를 위한 클래스(페이징 요청 정보와 함께 가상의 게시물 목록과 전체 개수를 가지고있음). (가상의 게시글 응답 객체들을 담은 리스트(responses), 페이지 요청 객체(pageRequest), 전체 게시글 수 (responses.size)를 넘겨주어 페이지 응답 객체를 생성.
       given(postService.readAllPost(any())).willReturn(pageResponses);
       //postService.readAllPost()메서드가 호출 될 때 어떤 인자가 전달되든 상관없이 가상의 페이지 응답 객체 'pageResponses' 를 반환하도록 설정.



       //when & then
       mockMvc.perform(get("/api/v1/posts"))    //api/v1/posts에 GET요청을 보낸다.
               .andExpect(status().isOk())  //반환된 http응답이 성공적으로 처리된것을 의미.
               .andExpect(jsonPath("$.content[0].postId").value(readPostResponse.getPostId())) //반환된 JSON 응답에서 content 배열의 첫 번째 요소의 postId 값을 가져와서 readPostResponse의 postId 값과 비교.
               .andExpect(jsonPath("$.content[0].title").value(readPostResponse.getTitle()))    //반환된 JSON 응답에서 content 배열의 title 값을 가져와서 readPostResponse의 title 값과 비교.
               .andExpect(jsonPath("$.content[0].content").value(readPostResponse.getContent()))    //반환된 JSON 응답에서 content 배열의 content 값을 가져와서 readPostResponse의 content 값과 비교.
               .andDo(print()); //테스트 수행 중에 요청과 응답에 대한 정보를 콘솔에 출력하는 역할. (테스트 중 발생한 요청과 응답의 내용을 확인)

   }
}


