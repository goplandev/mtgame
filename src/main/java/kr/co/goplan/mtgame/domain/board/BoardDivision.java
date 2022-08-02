package kr.co.goplan.mtgame.domain.board;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "board_division")
@Getter
@Setter
@Entity(name = "BoardDivision")
public class BoardDivision {
    @Id
    @Column(name = "board_division_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private LocalDateTime registeredDatetime;

    private LocalDateTime modifiedDatetime;

    private int isDelete;

}
