package com.cydeo.entity;

import com.cydeo.enums.Gender;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
@Entity
//@Where(clause = "is_deleted=false") // this annotation allows to delete obj from UI and keep it in database
public class User extends BaseEntity {

    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String userName;

    private String passWord;
    private boolean enabled;
    private String phone;

    @ManyToOne // without this annotation spring does not know where to put foreign key
    private Role role;

    @Enumerated(EnumType.STRING) // without this annotation, by default will be ordinal
    private Gender gender;





}
