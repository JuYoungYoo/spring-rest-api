package com.juyoung.webserver.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass // BaseTimeEntity 상속할 경우 createDate, modifiedDate 컬럼 mapping
@EntityListeners(AuditingEntityListener.class)  // Auditing
// 모든 상위 클래스 : createDate, modifiedDate 관리
public abstract class BaseTimeEntity{

    @CreatedBy
    @Column(name="CREATE_BY")
    private Long createdBy;
    @CreatedDate
    private LocalDateTime createDate;
    @LastModifiedDate
    private LocalDateTime modifiedDate;

}
