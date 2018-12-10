package com.juyoung.webserver.dao;

import com.juyoung.webserver.domain.Boards;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

// DAO
// @Repository 선언 X
// JpaRepository<Entity클래스, PK타입> : CURD 자동 생성
//@Repository
@RepositoryRestResource(path ="boards")
public interface BoardsRepository extends JpaRepository<Boards, Long>{
    Boards findByAuthor(String author);
}
