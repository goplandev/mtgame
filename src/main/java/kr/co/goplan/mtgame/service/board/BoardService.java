package kr.co.goplan.mtgame.service.board;

import kr.co.goplan.mtgame.domain.board.Board;
import kr.co.goplan.mtgame.domain.board.BoardDto;
import kr.co.goplan.mtgame.domain.contents.Contents;
import kr.co.goplan.mtgame.domain.file.FileInfo;
import kr.co.goplan.mtgame.domain.member.MemberDto;
import kr.co.goplan.mtgame.repository.board.BoardRepository;
import kr.co.goplan.mtgame.repository.file.FileInfoRepository;
import kr.co.goplan.mtgame.util.FileUtilities;
import kr.co.goplan.mtgame.util.paging.Paged;
import kr.co.goplan.mtgame.util.paging.Paging;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Getter
@Setter
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;
    private final FileInfoRepository fileInfoRepository;

    @Value("#{config['upload.dir']}")
    private String uploadDir;// = "D:/upload/mtgame/";

    @Value("#{config['video.allow']}")
    private String videoAllow;
    @Value("#{config['image.allow']}")
    private String imageAllow;
    @Value("#{config['audio.allow']}")
    private String audioAllow;
    @Value("#{config['ppt.allow']}")
    private String pptAllow;

    @Transactional //변경
    public Long save(BoardDto boardDto) {
        FileUtilities.allowSet(uploadDir,videoAllow,imageAllow,audioAllow,pptAllow);
        boardRepository.save(boardDto.toEntity());

        return boardDto.getId();
    }
    @Transactional
    public Long save(BoardDto boardDto , List<MultipartFile> files , List<Long> deleteFileList) throws Exception{
        FileUtilities.allowSet(uploadDir,videoAllow,imageAllow,audioAllow,pptAllow);
        Board board = boardRepository.save(boardDto.toEntity());

        List<FileInfo> fileInfoList = FileUtilities.parseFileInfo(files, board);

        // 파일이 존재할 경우
        if (!fileInfoList.isEmpty()) {
            fileInfoList.forEach(attachments -> fileInfoRepository.save(attachments));
        }
        // 삭제할 파일이 존재할 경우
        if (!deleteFileList.isEmpty()) {
            fileInfoRepository.deleteByAttachIdList(deleteFileList);
        }
        return board.getId();
    }
    @Transactional //변경
    public Long edit(BoardDto boardDto) {
        boardRepository.save(boardDto.toEntity());
        return boardDto.getId();
    }
    public BoardDto findOne(Long id){
        //return memberRepository.findOne(id);
        //JpaRepository 사용시
        BoardDto boardDto = new BoardDto(boardRepository.findById(id).get());
        return boardDto;
    }
    public Paged<BoardDto> boardSearchPage(int pageNumber, int size, String keyword) {
        PageRequest request = PageRequest.of(pageNumber -1 , size , Sort.Direction.DESC,"id");
        Page<BoardDto> boardList;
        if(keyword.equals("")){
            boardList = boardRepository.findAllDel(request);
        }else{
            boardList = boardRepository.findByTitleSearch(keyword,request);
        }
        return new Paged<>(boardList, Paging.of(boardList.getTotalPages(), pageNumber, size));
    }
}
