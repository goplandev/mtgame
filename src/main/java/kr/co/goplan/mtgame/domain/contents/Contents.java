package kr.co.goplan.mtgame.domain.contents;

import kr.co.goplan.mtgame.constant.ContentsType;
import kr.co.goplan.mtgame.constant.DfTimeEntity;
import kr.co.goplan.mtgame.domain.BaseEntity;
import kr.co.goplan.mtgame.domain.board.BoardDivision;
import kr.co.goplan.mtgame.domain.file.FileInfo;
import kr.co.goplan.mtgame.domain.member.Member;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@Entity
@Table(name = "contents" )
public class Contents extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @OneToMany
    @JoinTable(name="contents_captures",
            joinColumns = { @JoinColumn(name = "contents") },
            inverseJoinColumns = { @JoinColumn(name = "captures") })
    private Set<FileInfo> captures = new HashSet<FileInfo>();

    private String title;

    private String creator;

    private String thumb_path;

    private String file_path;

    private String description;

//    @Temporal(TemporalType.TIMESTAMP)
//    @DateTimeFormat(style = "M-")
 //   private LocalDateTime registeredDatetime;

 //   @ManyToOne(fetch = FetchType.LAZY)
 //   @JoinColumn(name = "register_id")
//    private Member register;

//    @Temporal(TemporalType.TIMESTAMP)
//    @DateTimeFormat(style = "M-")
 //   private LocalDateTime modifiedDatetime;

//    @ManyToOne(fetch = FetchType.LAZY)
 //   @JoinColumn(name = "modifier_id")
//    private Member modifier;

//    @Temporal(TemporalType.TIMESTAMP)
//    @DateTimeFormat(style = "M-")
//    private LocalDateTime endDate;

    private Boolean isUsed = true;

    private Boolean isDeleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fileInfo_id")
    private FileInfo fileInfo;

    private Long fileId;
    private String fileName;
    private String contentTypeStr;

    @Enumerated
    private ContentsType contentType;

    @ManyToOne
    @JoinColumn(name = "preview")
    private FileInfo preview;

    @ManyToOne
    @JoinColumn(name = "previewVideo")
    private FileInfo previewVideo;

    @Builder
    public Contents(Contents contents) {
        this.id = contents.getId();
        this.title = contents.getTitle();
        this.creator = contents.getCreator();
        //this.register = contents.getRegister();
        //this.modifier = contents.getModifier();
        this.description = contents.getDescription();
        //this.registeredDatetime = contents.getRegisteredDatetime();
        //this.modifiedDatetime = contents.getModifiedDatetime();
        //this.register = contents.getRegister();
        //this.modifier = contents.getModifier();
        this.thumb_path = contents.getThumb_path();
        this.file_path = contents.getFile_path();
        this.fileId = contents.getId();
        this.fileName = contents.getFileName();
        this.contentType = contents.getContentType();
        this.contentTypeStr = contents.getContentTypeStr();
    }

    public Contents(){

    }
}
