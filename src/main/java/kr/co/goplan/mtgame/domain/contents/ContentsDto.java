package kr.co.goplan.mtgame.domain.contents;

import kr.co.goplan.mtgame.domain.board.Board;
import kr.co.goplan.mtgame.domain.file.FileInfo;
import kr.co.goplan.mtgame.domain.member.Member;
import lombok.*;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ContentsDto {

    private Long id;

    /*private Member register;
    private Member modifier;*/
    private String creator;
    private String title;
    private String description;
    //private LocalDateTime registeredDatetime;
    //private LocalDateTime modifiedDatetime;
    private String createdBy;
    private String lastModifiedBy;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private Boolean isDeleted = false;
    private FileInfo fileInfo;
    private Long fileId;
    private String fileName;
    private FileInfo preview;
    /*public Contents toEntity() {
        return Contents.builder().contents();
    }*/

    public ContentsDto(Contents contents) {
        this.id = contents.getId();
        this.title = contents.getTitle();
        this.creator = contents.getCreator();
        this.description = contents.getDescription();
        this.createdBy = contents.getCreatedBy();
        this.lastModifiedBy = contents.getLastModifiedBy();
        this.createdDate = contents.getCreatedDate();
        this.lastModifiedDate = contents.getLastModifiedDate();
        this.isDeleted = contents.getIsDeleted();
        this.fileInfo = contents.getFileInfo();
        this.fileId = contents.getFileId();
        this.fileName = contents.getFileName();
        this.preview = contents.getPreview();
    }
    /*@Getter
    @NoArgsConstructor
    public static class Response{
        private Long id;
        private String memberName;
        private String creator;
        private String title;
        private String description;
        private LocalDateTime registeredDatetime;
        private FileInfo fileInfo;

        public Response(Contents contents){
            this.id = contents.getId();
            //this.memberName = content.getMember().getName();
            this.creator = contents.getCreator();
            this.description = contents.getDescription();
            this.title = contents.getTitle();
            this.registeredDatetime = contents.getRegisteredDatetime();
            this.fileInfo = contents.getFileInfo();
        }
    }

    @Getter
    public static class ListResponse{
        private Long id;
        private String memberName;
        private String title;
        private String creator;
        private LocalDateTime registeredDatetime;
        private FileInfo fileInfo;
        public ListResponse(Contents contents){
            this.id = contents.getId();
            //this.memberName = content.getMember().getName();
            this.title = contents.getTitle();
            this.creator = contents.getCreator();
            this.registeredDatetime = contents.getRegisteredDatetime();
            this.fileInfo = contents.getFileInfo();
        }
    }*/

}
