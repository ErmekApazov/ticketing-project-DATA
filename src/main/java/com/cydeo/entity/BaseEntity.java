package com.cydeo.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass // allows an entity to inherit properties from a base class

public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean isDeleted = false; //


    @Column(nullable = false, updatable = false) // prevent from storing null value and updating the table
    private LocalDateTime insertDateTime;

    @Column(nullable = false, updatable = false) // i.e. do not touch table when saving, updating
    private Long insertUserId;

    @Column(nullable = false)
    private LocalDateTime lastUpdateDateTime;

    @Column(nullable = false)
    private Long lastUpdateUserId;

    @PrePersist
    private void onPrePersist(){ // exe when we create new user/object. it shows date of saving,create, etc.
        this.insertDateTime = LocalDateTime.now();
        this.lastUpdateDateTime = LocalDateTime.now();
        this.insertUserId = 1L;
        this.lastUpdateUserId = 1L;
    }
    @PreUpdate
    private void onPreUpdate(){ // exe when we update the user/object. it shows date of update
        this.lastUpdateDateTime = LocalDateTime.now();
        this.lastUpdateUserId = 1L;
    }

}
