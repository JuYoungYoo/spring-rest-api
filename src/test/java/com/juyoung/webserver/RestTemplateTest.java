package com.juyoung.webserver;

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

    @LocalServerPort // EmbeddedWebApplicationContext load
    private int port;
    private URL apiURL;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setUp() throws Exception{
        this.apiURL = new URL("http://localhost:" + port + "/api/boards");
    }

    @Test
    public void  get_board_list() throws Exception{
        // url : http://localhost:port/api/boards
        ResponseEntity<List> response = restTemplate.getForEntity(apiURL.toString(), List.class);
        Assert.assertEquals(response.getStatusCode(),HttpStatus.OK);
        logger.info("::::::response: " +response.getBody());
    }

    @Test
    public void get_board_by_id() throws Exception{
        // url : http://localhost:port/api/boards/1
        long id = 1;
        ResponseEntity<Boards> response = restTemplate.getForEntity(apiURL.toString() + "/" + id,Boards.class);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assert.assertNotNull(response.getBody());
        logger.info(":::::: response : " + response.getBody());
    }

    // exchange
    @Test
    public void get_board_by_id2() throws Exception{
        // url : http://localhost:port/api/boards/1
        // exchange : http header, return httpResponseEntity
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:" + port + "/api/boards/1");
//                .queryParam("", orderNo);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        Boards response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, httpEntity, Boards.class).getBody();
        logger.info("response : " + response);
    }

    @Test
    public void create_board(){
        //when
        Boards board = Boards.builder()
                .title("add title")
                .author("add userName")
                .content("add content")
                .build();

        ResponseEntity<Boards> response = restTemplate.postForEntity(apiURL.toString(), board, Boards.class);
        System.out.println("response : " + response);
    }

    // delete, exchange, excute
    // return, parameter value X
    // > 필요시 exchange 사용
    // 방법 [1] delete
    @Test
    public void delete_board(){
        // http headers, http Entity use
        long id = 1;
        URI uri = URI.create(apiURL + "/" + id);
        restTemplate.delete(uri);
    }
    // 방법 [2] return 필요시 exchange
    @Test
    public void delete2_board(){
        long id = 1;
        URI uri = URI.create(apiURL + "/" + id);
        HttpHeaders headers = new HttpHeaders();
        HttpEntity entity = new HttpEntity(headers);

        ResponseEntity<Object> responseEntity = restTemplate.exchange(uri, HttpMethod.DELETE, entity, Object.class);
        Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        logger.info(responseEntity.getBody());
    }

    // delete와 동일, 차이점 : +객체
    // + httpEntity + header
    @Test
    public void put_board(){
        long id = 1;
        URI uri = URI.create(apiURL + "/" + id);

        Boards board = Boards.builder().author("update author")
                .content("update content")
                .title("update title")
                .build();


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Boards> entity = new HttpEntity<>(board, headers);

        ResponseEntity<Boards> responseEntity = restTemplate.exchange(uri, HttpMethod.PUT, entity, Boards.class);

        Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        logger.info("::::body : " +  responseEntity.getBody());
    }

}

// getForObject(), postForObject() : object mapping
// getForEntity(), postForEntity() : httpEntity()
// delete(), put() : return X
// exchange(), execute() : 모든 함수 Http method 요청 사용가능
// PATCH : 지원 X : HttpClient 라이브러리 HttpComponentsClientHttpRequestFactory 이용