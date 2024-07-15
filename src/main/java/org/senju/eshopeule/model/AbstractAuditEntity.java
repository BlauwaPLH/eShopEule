package org.senju.eshopeule.model;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.senju.eshopeule.listener.JpaAuditingEntityListener;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(JpaAuditingEntityListener.class)
public class AbstractAuditEntity implements BaseEntity {

    @CreationTimestamp
    private LocalDateTime createdOn;

    @CreatedBy
    private String createdBy;

    @UpdateTimestamp
    private LocalDateTime lastModifiedOn;

    @LastModifiedBy
    private String lastModifiedBy;
}
