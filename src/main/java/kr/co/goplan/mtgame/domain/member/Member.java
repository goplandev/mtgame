package kr.co.goplan.mtgame.domain.member;

import kr.co.goplan.mtgame.domain.BaseEntity;
import kr.co.goplan.mtgame.domain.group.Group;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "member" , uniqueConstraints = {@UniqueConstraint(name = "EMAIL_UNIQUE" , columnNames = {"EMAIL"})})
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    // name
    private String name;
    // password
    private String password;
    // email
    private String email;

    // phone
    private String phone;

    private Role role;

    // is_delete
    private int isDelete;

    private String newpassword;

    @Builder
    public Member(Long id, String name , String password , String email , String phone ,Role role,int isDelete){
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.isDelete = isDelete;
    }



    public Member() {

    }
}
