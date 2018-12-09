package com.juyoung.webserver.controller;

import com.juyoung.webserver.domain.Boards;
import com.juyoung.webserver.service.BoardsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@AllArgsConstructor
public class BoardController {

    // board dao
    private BoardsService service;

    @GetMapping(value = "/api/boards")
    public List<Boards> list() {
        return service.list();
    }

    @PostMapping("/api/boards")
    public ResponseEntity<Boards> create(@RequestBody Boards dto) {
        try {
            Boards board = service.create(dto);
            long id = dto.getSeq();
            board.add(linkTo(methodOn(BoardController.class).list()).withRel("list"),
                    linkTo(methodOn(BoardController.class).show(id)).withRel("read"));
            return new ResponseEntity<>(board, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/api/boards/{id}")
    public ResponseEntity<Boards> show(@PathVariable("id") Long id) {
        Boards board = service.read(id);
        board.add(linkTo(methodOn(BoardController.class).show(id)).withSelfRel(),
                linkTo(methodOn(BoardController.class).list()).withRel("list"));
        return new ResponseEntity<>(board, HttpStatus.OK);
    }

    @PutMapping("/api/boards/{id}")
    public Boards update(@PathVariable("id") long id, @RequestBody Boards dto) {
        Boards boards = service.update(id, dto);
        return boards;
    }

    @DeleteMapping("/api/boards/{id}")
    public void delete(@PathVariable("id") long id) {
        service.delete(id);
    }
}
