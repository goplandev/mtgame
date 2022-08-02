package kr.co.goplan.mtgame.api.board;

import com.google.gson.JsonObject;
import kr.co.goplan.mtgame.api.contents.ContentsApiController;
import kr.co.goplan.mtgame.domain.board.Board;
import kr.co.goplan.mtgame.domain.board.BoardDivision;
import kr.co.goplan.mtgame.domain.board.BoardDto;
import kr.co.goplan.mtgame.domain.contents.Contents;
import kr.co.goplan.mtgame.domain.file.FileInfo;
import kr.co.goplan.mtgame.domain.member.Member;
import kr.co.goplan.mtgame.service.board.BoardDivisionService;
import kr.co.goplan.mtgame.service.board.BoardService;
import kr.co.goplan.mtgame.service.member.MemberService;
import kr.co.goplan.mtgame.util.DateUtil;
import kr.co.goplan.mtgame.util.FileUtilities;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class BoardApiController {

    private static final Logger logger = LoggerFactory.getLogger(BoardApiController.class);

    private final BoardService boardService;
    private final BoardDivisionService boardDivisionService;
    private final MemberService memberService;


    @PostMapping("/boards/new")
    @ResponseStatus(HttpStatus.CREATED)
    public String saveContent(MultipartHttpServletRequest multiRequest) throws Exception
    {

        logger.info(multiRequest.getParameter("id"));
        JsonObject jsonObject = new JsonObject();
        // 작성자 정보 확인 - 로그인처리등 활성화시 주석제거
        /*Optional<Member> optMember = memberService.getMemberRepository().findByName(multiRequest.getParameter("creator"));

        if (!optMember.isPresent()) {
            jsonObject.addProperty("response", "error");
            jsonObject.addProperty("errorMsg", "사용자 정보를 찾을 수 없습니다.");
            return jsonObject.toString();
        }*/
        BoardDto boardDto = new BoardDto();
        List<Long> deleteFileList = new ArrayList<>();
        // 신규 등록
        if (multiRequest.getParameter("id") == null) {
            //content.setMember(optMember.get());
            //Member member = memberService.findMemberOne(1l).toEntity();
            //boardDto.setRegister(member);
            //boardDto.setModifier(member);
            boardDto.setTitle(multiRequest.getParameter("title"));
            boardDto.setDescription(multiRequest.getParameter("description"));
            //contents.setModifier(optMember);
            //boardDto.setRegisteredDatetime(DateUtil.getUTCDate());
            BoardDivision boardDivision = boardDivisionService.findOne(Long.parseLong(multiRequest.getParameter("boardDivision")));

            boardDto.setBoardDivision(boardDivision);

        }
        // 수정
        else {
            //Optional<Board> content1 = boardService.getBoardRepository().findById(Long.parseLong(multiRequest.getParameter("id")));
            boardDto = new BoardDto(boardService.getBoardRepository().findById(Long.parseLong(multiRequest.getParameter("id"))).get());
            if (boardDto != null) {
                //boardDto = content1.get();
                Member member = memberService.findMemberOne(1l).toEntity();
                //boardDto.setRegister(member);
                boardDto.setTitle(multiRequest.getParameter("title"));
                //boardDto.setDescription(multiRequest.getParameter("description"));
                //boardDto.setModifiedDatetime(DateUtil.getUTCDate());
                //contents.setRegister(optMember);
                if (multiRequest.getParameter("deleteFiles") != null) {
                    deleteFileList = Arrays.asList(multiRequest.getParameter("deleteFiles").split(",")).stream() .map(s -> Long.parseLong((String) s)).collect(Collectors.toList());
                }
            }else{
                jsonObject.addProperty("response", "error");
                jsonObject.addProperty("errorMsg", "게시물 정보를 찾을 수 없습니다.");
                return jsonObject.toString();
            }

        }
        Long id = boardService.save(boardDto, multiRequest.getFiles("files"), deleteFileList);

        jsonObject.addProperty("response", "OK");
        jsonObject.addProperty("content_id", id);
        return jsonObject.toString();

    }
    //첨부파일 다운로드
    @GetMapping("/boards/download/{id}")
    public void downloadContent(@PathVariable Long id, HttpServletResponse response){

        Optional<FileInfo> fileInfo = boardService.getFileInfoRepository().findById(id);
        if (!fileInfo.isPresent()) {
            throw new RuntimeException("파일을 찾을 수 없습니다.");
        }
        FileInfo attach = fileInfo.get();
        File file = FileUtilities.getDownloadFile(attach);

        try {
            byte[] data = FileUtils.readFileToByteArray(file);
            response.setContentType(FileUtilities.getMediaType(attach.getOriginalFilename()).toString());
            response.setContentLength(data.length);
            response.setHeader("Content-Transfer-Encoding", "binary");
            response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(attach.getOriginalFilename(), "UTF-8") + "\";");
            response.getOutputStream().write(data);
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (IOException e) {
            throw new RuntimeException("파일 다운로드에 실패하였습니다.");
        } catch (Exception e) {
            throw new RuntimeException("시스템에 문제가 발생하였습니다.");
        }

    }
    //Display Img
    @GetMapping(value = "/boards/display")
    public ResponseEntity<byte[]> displayImgFile(@RequestParam("id") Long id)throws Exception{
        InputStream in = null;
        ResponseEntity<byte[]> entity = null;

        Optional<FileInfo> optAttach = boardService.getFileInfoRepository().findById(id);

        if(!optAttach.isPresent()) {
            new RuntimeException("이미지 정보를 찾을 수 없습니다.");
        }

        FileInfo attach = optAttach.get();

        try {
            HttpHeaders headers = new HttpHeaders();
            in = new FileInputStream(attach.getFilePath());
            headers.setContentType(FileUtilities.getMediaType(attach.getOriginalFilename()));
            headers.add("Content-Disposition", "attachment; filename=\"" + new String(attach.getOriginalFilename().getBytes("UTF-8"), "ISO-8859-1")+"\"");
            entity = new ResponseEntity<byte[]>(IOUtils.toByteArray(in), headers, HttpStatus.CREATED);
        } catch(Exception e) {
            e.printStackTrace();
            entity = new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
        } finally {
            in.close();
        }
        return entity;
    }
}
