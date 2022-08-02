package kr.co.goplan.mtgame.domain.board;

import kr.co.goplan.mtgame.domain.BaseEntity;
import kr.co.goplan.mtgame.domain.member.Member;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "board")
public class Board extends BaseEntity {
    @Id
    @Column(name = "board_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String creator;

    /*@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "register_id")
    private Member register;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modifier_id")
    private Member modifier;*/

    private String title;

    private String description;

    private Boolean isDeleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_division_id")
    private BoardDivision boardDivision;

    @Builder
    //public Board(Long id, String title,String creator,Member register,Member modifier, String description, LocalDateTime registeredDatetime, LocalDateTime modifiedDatetime,Boolean isDeleted, BoardDivision boardDivision) {
    public Board(Long id, String title,String creator, String description,Boolean isDeleted, BoardDivision boardDivision) {
        this.id = id;
        this.title = title;
        this.creator = creator;
        this.description = description;
        this.isDeleted = isDeleted;
        this.boardDivision = boardDivision;
    }

    public Board(){

    }
}
