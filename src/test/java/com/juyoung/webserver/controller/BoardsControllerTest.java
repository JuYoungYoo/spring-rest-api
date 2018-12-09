package com.juyoung.webserver.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.juyoung.webserver.domain.Boards;
import com.juyoung.webserver.service.BoardsService;
import com.sun.istack.internal.logging.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class BoardsControllerTest {

    Logger logger = Logger.getLogger(this.getClass());

    private MockMvc mockMvc;

    @Autowired
    private BoardController controller;

    @Autowired
    private BoardsService service;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    private String jsonStringFromObject(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

    @Test
    public void getList() throws Exception {
        List<Boards> boardsList = service.list();

        String jsonString = this.jsonStringFromObject(boardsList);
        logger.info(jsonString + " :::::");

        mockMvc.perform(get("/api/boards"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(jsonString))
                .andDo(print())
                .andReturn();
    }

    @Test
    public void show() throws Exception {
        long id = 1;
        Boards board = service.read(id);
        mockMvc.perform(get("/api/boards/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    @Test
    public void create() throws Exception {
        Boards board = Boards.builder()
                .author("test")
                .content("content")
                .title("title").build();
        String jsonString = this.jsonStringFromObject(board);
        MvcResult result = mockMvc.perform(post("/api/boards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }


}
