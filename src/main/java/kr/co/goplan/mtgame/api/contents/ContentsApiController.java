package kr.co.goplan.mtgame.api.contents;

import com.google.gson.JsonObject;

import kr.co.goplan.mtgame.constant.ContentsType;
import kr.co.goplan.mtgame.domain.contents.Contents;
import kr.co.goplan.mtgame.domain.file.FileInfo;
import kr.co.goplan.mtgame.domain.member.Member;
import kr.co.goplan.mtgame.service.contents.ContentsService;
import kr.co.goplan.mtgame.service.member.MemberService;
import kr.co.goplan.mtgame.util.DateUtil;
import kr.co.goplan.mtgame.util.FileUtilities;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@RestController

public class ContentsApiController {

    private static final Logger logger = LoggerFactory.getLogger(ContentsApiController.class);

    private final ContentsService contentsService;
    private final MemberService memberService;
    ////////////////         contents    /////////////////////
    //삭제
    @PostMapping("/contents/delete")
    public void deleteContent(@RequestParam("content_id") Long content_id){
        contentsService.delete(content_id);
    }

    //저장
    @PostMapping("/contents")
    @ResponseStatus(HttpStatus.CREATED)
    public String saveContent(MultipartHttpServletRequest multiRequest) throws Exception
    {

        logger.info(multiRequest.getParameter("id"));
        JsonObject jsonObject = new JsonObject();
        // 작성자 정보 확인
        /*Optional<Member> optMember = memberService.getMemberRepository().findByName(multiRequest.getParameter("creator"));

        if (!optMember.isPresent()) {
            jsonObject.addProperty("response", "error");
            jsonObject.addProperty("errorMsg", "사용자 정보를 찾을 수 없습니다.");
            return jsonObject.toString();
        }*/

        Contents contents = new Contents();
        List<Long> deleteFileList = new ArrayList<>();
        // 신규 등록
        if (multiRequest.getParameter("id") == null) {
            //content.setMember(optMember.get());
            contents.setTitle(multiRequest.getParameter("title"));
            //contents.setRegisteredDatetime(DateUtil.getUTCDate());
        }
        // 수정
        else {
            Optional<Contents> content1 = contentsService.getContentsRepository().findById(Long.parseLong(multiRequest.getParameter("id")));
            if (!content1.isPresent()) {
                jsonObject.addProperty("response", "error");
                jsonObject.addProperty("errorMsg", "게시물 정보를 찾을 수 없습니다.");
                return jsonObject.toString();
            }
            contents = content1.get();
            contents.setTitle(multiRequest.getParameter("title"));
            //contents.setContent(multiRequest.getParameter("content"));
            //contents.setModifiedDatetime(DateUtil.getUTCDate());
            //contents.setRegister(optMember);
            if (multiRequest.getParameter("deleteFiles") != null) {
                deleteFileList = Arrays.asList(multiRequest.getParameter("deleteFiles").split(",")).stream() .map(s -> Long.parseLong((String) s)).collect(Collectors.toList());
            }
        }
        Long id = contentsService.save(contents, multiRequest.getFiles("files"), deleteFileList);
        jsonObject.addProperty("response", "OK");
        jsonObject.addProperty("content_id", id);
        return jsonObject.toString();

    }
    //시퀀스 연속 저장
    @PostMapping("/contentsSeq")
    @ResponseStatus(HttpStatus.CREATED)
    public String saveContentSeq(MultipartHttpServletRequest multiRequest) throws Exception
    {

        logger.info(multiRequest.getParameter("id"));
        JsonObject jsonObject = new JsonObject();
        // 작성자 정보 확인
        /*Optional<Member> optMember = memberService.getMemberRepository().findByName(multiRequest.getParameter("creator"));

        if (!optMember.isPresent()) {
            jsonObject.addProperty("response", "error");
            jsonObject.addProperty("errorMsg", "사용자 정보를 찾을 수 없습니다.");
            return jsonObject.toString();
        }*/

        Contents contents = new Contents();
        List<Long> deleteFileList = new ArrayList<>();
        // 신규 등록
        if (multiRequest.getParameter("id") == null) {
            Member member = memberService.findMemberOne(1l).toEntity();
            //contents.setRegister(member);
            //contents.setModifier(member);
            contents.setTitle(multiRequest.getParameter("title"));
            //contents.setRegisteredDatetime(DateUtil.getUTCDate());
        }
        // 수정
        else {
            Member member = memberService.findMemberOne(1l).toEntity();
            //contents.setModifier(member);
            Optional<Contents> content1 = contentsService.getContentsRepository().findById(Long.parseLong(multiRequest.getParameter("id")));
            if (!content1.isPresent()) {
                jsonObject.addProperty("response", "error");
                jsonObject.addProperty("errorMsg", "게시물 정보를 찾을 수 없습니다.");
                return jsonObject.toString();
            }

            contents = content1.get();

            contents.setTitle(multiRequest.getParameter("title"));
            //contents.setContent(multiRequest.getParameter("content"));
            //contents.setModifiedDatetime(DateUtil.getUTCDate());

        }
        //파일 종류에 따른 개별 컨텐츠 생성 저장
        Long id = contentsService.savefileSeq(contents, multiRequest.getFiles("files"), deleteFileList);
        jsonObject.addProperty("response", "OK");
        jsonObject.addProperty("content_id", id);
        return jsonObject.toString();

    }

    //첨부파일 다운로드
    @GetMapping("/contents/download/{id}")
    public void downloadContent(@PathVariable Long id, HttpServletResponse response){

        Optional<FileInfo> fileInfo = contentsService.getFileInfoRepository().findById(id);
        if (!fileInfo.isPresent()) {
            throw new RuntimeException("파일을 찾을 수 없습니다.");
        }
        FileInfo attach = fileInfo.get();
        File file = FileUtilities.getDownloadFile(attach);
        
        //영상 다운로드 에러 나는것 수정해야 함
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

}
