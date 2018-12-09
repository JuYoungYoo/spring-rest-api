package com.juyoung.webserver.service;

import com.juyoung.webserver.dao.BoardsRepository;
import com.juyoung.webserver.domain.Boards;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardsService {

    @Autowired
    BoardsRepository boardDAO;

    public List<Boards> list() {
        return boardDAO.findAll();
    }

    public Boards read(long id) {
        return boardDAO.getOne(id);
    }

    public Boards create(Boards board) {
        return boardDAO.save(board);
    }

    public Boards update(long id, Boards newBoard) {
        return boardDAO.findById(id).map(boards -> {
            boards.setAuthor(newBoard.getAuthor());
            boards.setContent(newBoard.getContent());
            boards.setTitle(newBoard.getTitle());
            return boardDAO.save(boards);
        })
        .orElseGet(() -> {
                    newBoard.setSeq(id);
                    return boardDAO.save(newBoard);
        });
    }

    public void delete(long id) {
        boardDAO.deleteById(id);
    }
}
