package org.senju.eshopeule.model;


import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.senju.eshopeule.listener.MongoAuditingEntityListener;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(MongoAuditingEntityListener.class)
public class AbstractAuditDocument implements BaseEntity {

    @CreatedDate
    private LocalDateTime createdOn;

    @CreatedBy
    private String createdBy;

    @CreatedDate
    private LocalDateTime lastModifiedOn;

    @LastModifiedBy
    private String lastModifiedBy;
}
