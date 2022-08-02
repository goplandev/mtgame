package kr.co.goplan.mtgame.repository.file;

import kr.co.goplan.mtgame.constant.ContentsType;
import kr.co.goplan.mtgame.domain.file.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface FileInfoRepository extends JpaRepository<FileInfo , Long> {

    public List<FileInfo> findAllByBoardId(Long boardId);
    //public List<FileInfo> findAllByFileIn(Long contentsId);
    public List<FileInfo> findAllByContentsId(Long boardId);

    @Modifying
    @Query(value = "DELETE FROM FileInfo a " + "WHERE a.id IN (:deleteFileList)")
    public void deleteByAttachIdList(@Param("deleteFileList") List<Long> deleteFileList);

    @Query("select o from FileInfo as o where o.fileId = :fileId ")
    FileInfo findFileInfoByFileId(@Param("fileId") Long fileId);

    @Query("select o from FileInfo as o where o.mtime is null ")
    List<FileInfo> findFileInfosByMTimeNull();

    @Query("select o from FileInfo as o where o.name = :filename ")
    List<FileInfo> findFileInfosByFileName(@Param("filename") String filename);

    @Query("select o from FileInfo as o where o.contentType = :contentType ")
    List<FileInfo> findAllByContentsType(@Param("contentType") ContentsType contentType);

}
