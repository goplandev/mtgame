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
        // ????????? ?????? ?????? - ?????????????????? ???????????? ????????????
        /*Optional<Member> optMember = memberService.getMemberRepository().findByName(multiRequest.getParameter("creator"));

        if (!optMember.isPresent()) {
            jsonObject.addProperty("response", "error");
            jsonObject.addProperty("errorMsg", "????????? ????????? ?????? ??? ????????????.");
            return jsonObject.toString();
        }*/
        BoardDto boardDto = new BoardDto();
        List<Long> deleteFileList = new ArrayList<>();
        // ?????? ??????
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
        // ??????
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
                jsonObject.addProperty("errorMsg", "????????? ????????? ?????? ??? ????????????.");
                return jsonObject.toString();
            }

        }
        Long id = boardService.save(boardDto, multiRequest.getFiles("files"), deleteFileList);

        jsonObject.addProperty("response", "OK");
        jsonObject.addProperty("content_id", id);
        return jsonObject.toString();

    }
    //???????????? ????????????
    @GetMapping("/boards/download/{id}")
    public void downloadContent(@PathVariable Long id, HttpServletResponse response){

        Optional<FileInfo> fileInfo = boardService.getFileInfoRepository().findById(id);
        if (!fileInfo.isPresent()) {
            throw new RuntimeException("????????? ?????? ??? ????????????.");
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
            throw new RuntimeException("?????? ??????????????? ?????????????????????.");
        } catch (Exception e) {
            throw new RuntimeException("???????????? ????????? ?????????????????????.");
        }

    }
    //Display Img
    @GetMapping(value = "/boards/display")
    public ResponseEntity<byte[]> displayImgFile(@RequestParam("id") Long id)throws Exception{
        InputStream in = null;
        ResponseEntity<byte[]> entity = null;

        Optional<FileInfo> optAttach = boardService.getFileInfoRepository().findById(id);

        if(!optAttach.isPresent()) {
            new RuntimeException("????????? ????????? ?????? ??? ????????????.");
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
