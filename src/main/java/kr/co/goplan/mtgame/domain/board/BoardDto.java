package kr.co.goplan.mtgame.domain.board;

import kr.co.goplan.mtgame.domain.member.Member;
import kr.co.goplan.mtgame.domain.member.Role;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class BoardDto {
    private Long id;

    @NotEmpty(message = "제목은 필수 입니다")
    private String title;

    @NotEmpty(message = "작성자는 필수 입니다")
    private String creator;

    @NotEmpty(message = "내용은 필수 입니다")
    private String description;

    private Boolean isDeleted = false;
    private BoardDivision boardDivision;

    private String createdBy;
    private String lastModifiedBy;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    public Board toEntity() {
        return Board.builder()
                .id(id)
                .title(title)
                .description(description)
                .isDeleted(isDeleted)
                .boardDivision(boardDivision)
                .build();
    }

    @Builder
    public BoardDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.description = board.getDescription();
        this.creator = board.getCreator();
        this.createdBy = board.getCreatedBy();
        this.lastModifiedBy = board.getLastModifiedBy();
        this.createdDate = board.getCreatedDate();
        this.lastModifiedDate = board.getLastModifiedDate();
        this.isDeleted = board.getIsDeleted();
        this.boardDivision = board.getBoardDivision();
    }
}
