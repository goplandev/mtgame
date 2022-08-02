package kr.co.goplan.mtgame.domain.file;


import kr.co.goplan.mtgame.constant.ContentsType;
import kr.co.goplan.mtgame.domain.BaseEntity;
import kr.co.goplan.mtgame.domain.BaseTimeEntity;
import kr.co.goplan.mtgame.domain.board.Board;
import kr.co.goplan.mtgame.domain.board.BoardDivision;
import kr.co.goplan.mtgame.domain.contents.Contents;
import kr.co.goplan.mtgame.domain.member.Member;
import kr.co.goplan.mtgame.util.DateUtil;
import kr.co.goplan.mtgame.util.FileUtilities;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter@Setter
@NoArgsConstructor
@Entity(name = "FileInfo")
@Table(name = "file_info")
public class FileInfo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_info_id")
    private Long id;

    /*@ManyToOne
    @JoinColumn(name = "contents_id")
    private Contents contents;*/

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_division_id")
    private BoardDivision boardDivision;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contents_id")
    private Contents contents;

    private Long fileId;

    @Size(max = 200)
    private String filePath;

    private String serverFilePath;

    //@NotNull
    @Size(max = 2000)
    private String name;

    //@NotNull
    @Size(max = 2000)
    private String originalFilename;

    private String extension;

    private ContentsType contentType;

    private Long fileSize;

    private Boolean isDeleted = false;

    /*@Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")*/
    private LocalDateTime mtime = DateUtil.getUTCDate();

   /* @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")*/
    private LocalDateTime deletedDatetime;

    @ManyToOne
    @JoinColumn(name = "register_id")
    private Member register;

    @ManyToOne
    @JoinColumn(name = "modifier_id")
    private Member modifier;

    @Builder
    public FileInfo( Board board, String originalFilename , String name , String filePath , Long fileSize , String extension, String serverFilePath, BoardDivision boardDivision){
        //this.contents = content;
        this.board = board;
        this.name = name;
        this.originalFilename = originalFilename;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.extension = extension;
        this.serverFilePath = serverFilePath;
        this.boardDivision = boardDivision;

    }
    @Builder
    public FileInfo( String originalFilename , String name , String filePath , Long fileSize ,String extension , String serverFilePath){
        //this.contents = content;
        this.name = name;
        this.originalFilename = originalFilename;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.extension = extension;
        this.serverFilePath = serverFilePath;
    }

}

