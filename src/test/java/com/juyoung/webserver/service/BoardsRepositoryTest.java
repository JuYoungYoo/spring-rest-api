package com.juyoung.webserver.service;

import com.juyoung.webserver.dao.BoardsRepository;
import com.juyoung.webserver.domain.Boards;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BoardsRepositoryTest {

    @Autowired
    BoardsRepository boardsRepository; // dao

    // 저장
    @Test
    public void insert() {
        // given : 환경 구축, build @builder사용법
//        LocalDateTime createdTime = LocalDateTime.of(2018, 12, 3, 12, 11, 11);
//        LocalDateTime updateedTime = LocalDateTime.of(2018, 12, 3, 12, 11, 11);
        boardsRepository.save(Boards.builder()
                .title("제목")
                .content("내용")
                .author("글쓴이")
                .build());

        // when : 테스트 하고 자 하는 행위 : insert
        Boards board = boardsRepository.findByAuthor("글쓴이");

        // then : 테스트 결과 검증, DB insert 조회 후 확인
        Assert.assertEquals("제목", board.getTitle());
        Assert.assertEquals("내용", board.getContent());
        Assert.assertEquals("글쓴이", board.getAuthor());
    }

    @Test
    public void getBoards() throws Exception{
        List<Boards> boardList = boardsRepository.findAll();
        Assert.assertNotNull(boardList);
        Assert.assertEquals(boardList.size(),24);
    }

    @Test
    public void getBoard(){
        long id = 1;
        Boards board = boardsRepository.getOne(id);
        Assert.assertEquals(board.getContent(),"content1");
    }

    @Test
    public void delete(){
        long id = 2;
        List<Boards> before = boardsRepository.findAll();
        // when
        boardsRepository.deleteById(id);
        List<Boards> after = boardsRepository.findAll();
        // then
        Assert.assertEquals(before.size()-1, after.size());
    }


}
