package com.nhira.abnrecipeapp.model;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@MappedSuperclass
@ToString(of = {
        "id",
        "dateCreated",
        "lastUpdated"
}
)
public class BaseEntity {
    @Id
    protected String id;

    @CreationTimestamp
    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    protected OffsetDateTime dateCreated;

    @UpdateTimestamp
    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    protected OffsetDateTime lastUpdated;

    @PrePersist
    public void init() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
    }

}
