package com.juyoung.webserver.service;

import com.juyoung.webserver.dao.BoardsRepository;
import com.juyoung.webserver.domain.Boards;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BoardsRepositoryTest {

    @Autowired
    BoardsRepository boardsRepository; // dao

    @After
    public void cleanUp() {
//        boardsRepository.deleteAll();
    }

    // 저장
    @Test
    public void insert() {
        // given : 환경 구축, build @builder사용법
//        LocalDateTime createdTime = LocalDateTime.of(2018, 12, 3, 12, 11, 11);
//        LocalDateTime updateedTime = LocalDateTime.of(2018, 12, 3, 12, 11, 11);
        boardsRepository.save(Boards.builder()
                .title("title")
                .content("content")
                .author("username")
                .build());

        // when : 테스트 하고 자 하는 행위 : insert
        List<Boards> list = boardsRepository.findAll(); // Return post list

        // then : 테스트 결과 검증, DB insert 조회 후 확인
        Boards board = list.get(4);
        Assert.assertEquals("title", board.getTitle());
        Assert.assertEquals("content", board.getContent());
        Assert.assertEquals("username", board.getAuthor());
    }

    @Test
    public void getBoards() throws Exception{
        List<Boards> boardList = boardsRepository.findAll();
        Assert.assertNotNull(boardList);
        Assert.assertEquals(boardList.size(),4);
    }

    @Test
    public void getBoard(){
        long id = 1;
        Boards board = boardsRepository.getOne(id);
        Assert.assertEquals(board.getContent(),"내용1");
    }

    @Test
    public void delete(){
        long id = 1;
        List<Boards> before = boardsRepository.findAll();
        // when
        boardsRepository.deleteById(id);
        List<Boards> after = boardsRepository.findAll();
        // then
        Assert.assertEquals(before.size()-1, after.size());
    }


}
