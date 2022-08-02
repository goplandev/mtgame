package kr.co.goplan.mtgame.domain.app;

import kr.co.goplan.mtgame.domain.BaseEntity;
import kr.co.goplan.mtgame.domain.member.Member;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "app_info" )
public class AppInfo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    private String name;

    private String description;

    /*private LocalDateTime registeredDatetime;
    private LocalDateTime modifiedDatetime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "register_id")
    private Member register;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modifier_id")
    private Member modifier;

    private LocalDateTime endDate;
    private LocalDateTime startDate;*/

    private Boolean isUsed = true;

    private Boolean isDeleted = false;
}
