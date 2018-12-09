package com.juyoung.webserver.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.hateoas.ResourceSupport;

import javax.persistence.*;

//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@Getter
@Data
@Entity
//@RestResource
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
//public class Boards extends ResourceSupport{
public class Boards extends ResourceSupport{

    @Id  // PK
    @GeneratedValue(strategy = GenerationType.IDENTITY) // PK 규칙
    private Long seq; // bigint

    // Column 생략가능
    @Column(length = 500, nullable = false)
    private String title;
    @Column(columnDefinition = "TEXT", nullable = false) // columnDefinition : 컬럼 타입변경
    private String content;
    private String author;

    @Builder
    // 빌더 패턴 클래스 생성 : 생성자 상단에 선언 시 생성자에 포함된 필드만 빌더에 포함
    public Boards(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }
}