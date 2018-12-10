package com.juyoung.webserver.rest;

import com.juyoung.webserver.domain.Boards;
import org.jboss.logging.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URL;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestTemplateTest {

    Logger logger = Logger.getLogger(this.getClass());

    @LocalServerPort    // EmbeddedWebApplicationContext load
    private int port;
    private URL apiURL;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setUp() throws Exception {
        this.apiURL = new URL("http://localhost:" + port + "/api/boards");
    }

    /**
     * 게시판 리스트
     * METHOD : GET
     * URL : http://localhost:port/api/boards
     * restTemplate : getForEntity
     */
    @Test
    public void get_board_list() throws Exception {
        String url = apiURL.toString(); // http://localhost:port/api/boards
        ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        logger.info("::::::response: " + response.getBody());
    }

    /**
     * 게시판 내용보기 by id
     * METHOD : GET
     * URL : http://localhost:port/api/boards/{id}
     * restTemplate : getForEntity
     */
    @Test
    public void get_board_by_id() throws Exception {
        long id = 1;
        String url = apiURL.toString() + "/" + id; // http://localhost:port/api/boards/1
        ResponseEntity<Boards> response = restTemplate.getForEntity(url, Boards.class);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assert.assertNotNull(response.getBody());
        logger.info(":::::: response.getBody ::::: " + response.getBody());
    }

    /**
     * 게시판 내용보기 by id
     * METHOD : GET
     * URL : http://localhost:port/api/boards/1
     * RestTemplate : exchange
     */
    @Test
    public void get_board_by_id2() throws Exception {
        // url : http://localhost:port/api/boards/1
        // exchange : http header, return httpResponseEntity
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:" + port + "/api/boards/1");
//      .queryParam("", orderNo);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);

        Boards response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, httpEntity, Boards.class).getBody();
        logger.info(":::::: response.board ::::: " + response.toString());
    }

    /**
     * 게시판 등록
     * METHOD : POST
     * URL : http://localhost:port/api/boards
     * RestTemplate : postForEntity
     */
    @Test
    public void create_board() {
        //when
        String url = apiURL.toString(); // http://localhost:port/api/boards
        Boards board = Boards.builder()
                .title("add title")
                .author("add userName")
                .content("add content")
                .build();

        ResponseEntity<Boards> response = restTemplate.postForEntity(url, board, Boards.class);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        logger.info(":::::: response.getBody ::::: " + response.getBody());
    }

    /**
     * 방법[1] 게시판 삭제
     * METHOD : DELETE
     * URL : http://localhost:port/api/boards/{id}
     * RestTemplate : DELETE (DELETE, EXCHANGE, EXECUTE)
     * - RETURN parameter value X ( 필요 시 exchange 사용 )
     */
    @Test
    public void delete_board() {
        // http headers, http Entity use
        long id = 1;
        URI uri = URI.create(apiURL + "/" + id);
        restTemplate.delete(uri);
    }

    /**
     * 방법[2] 게시판 삭제
     * METHOD : DELETE
     * URL : http://localhost:port/api/boards/{id}
     * RestTemplate : Exchange
     * RETURN response
     */
    @Test
    public void delete2_board() {
        long id = 1;
        URI uri = URI.create(apiURL + "/" + id);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<Object> response = restTemplate.exchange(uri, HttpMethod.DELETE, entity, Object.class);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        logger.info(":::::: response.getBody ::::: " + response.getBody());
    }

    /**
     * 게시판 수정
     * METHOD : PUT
     * URL : http://localhost:port/api/boards/{id}
     * RestTemplate : PUT ( DELETE와 동일 / 차이점 : +HttpEntity, header )
     */
    @Test
    public void put_board() {
        long id = 1;
        URI uri = URI.create(apiURL + "/" + id);

        Boards board = Boards.builder().author("update author")
                .content("update content")
                .title("update title")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Boards> entity = new HttpEntity<>(board, headers);

        ResponseEntity<Boards> response = restTemplate.exchange(uri, HttpMethod.PUT, entity, Boards.class);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        logger.info(":::::: response.getBody ::::: " + response.getBody());
    }

}

// getForObject(), postForObject() : object mapping
// getForEntity(), postForEntity() : httpEntity()
// delete(), put() : return X
// exchange(), execute() : 모든 함수 Http method 요청 사용가능
// PATCH : 지원 X : HttpClient 라이브러리 HttpComponentsClientHttpRequestFactory 이용