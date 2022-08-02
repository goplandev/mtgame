package kr.co.goplan.mtgame.controller.contents;


import kr.co.goplan.mtgame.constant.AppConstants;
import kr.co.goplan.mtgame.constant.ContentsType;
import kr.co.goplan.mtgame.domain.contents.Contents;
import kr.co.goplan.mtgame.domain.contents.ContentsDto;
import kr.co.goplan.mtgame.domain.file.FileInfo;
import kr.co.goplan.mtgame.service.contents.ContentsService;
import kr.co.goplan.mtgame.util.FileUtilities;
import kr.co.goplan.mtgame.util.paging.Paged;
import lombok.AllArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/contents")
@AllArgsConstructor
public class ContentsController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ContentsService contentsService;

    //뷰
    @GetMapping("/{id}")
    public String searchById(@PathVariable Long id, Model model) {
        model.addAttribute("contentDto", contentsService.searchById(id));
        model.addAttribute("fileList", contentsService.getFileInfoRepository().findAllByContentsId(id));
        return "contents/viewContent";
    }
    //수정폼
    @GetMapping("/edit/{id}")
    public String editcontent(@PathVariable Long id, Model model) {
        // 관리자 or 작성자가 아닐 경우 수정 불가 로직
        model.addAttribute("contentDto", contentsService.searchById(id));
        model.addAttribute("fileList", contentsService.getFileInfoRepository().findAllByContentsId(id));
        return "contents/editContent";
    }
    //작성폼
    @GetMapping("/edit")
    public String newContent(Model model) {
        model.addAttribute("contentDto", new ContentsDto());
        return "contents/editContent";
    }
    //리스트
    @GetMapping("")
    public String listContentPage( @RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
                                        @RequestParam(value = "size", required = false, defaultValue = AppConstants.BoardSizeStr) int size ,
                                        Model model) {
        logger.info("----listcontent 호출");
        //일반
        //model.addAttribute("contents", contentsService.findAllByOrderByIdDescTypeFile(ContentsType.Image));
        //페이징
        Paged<ContentsDto> contentsDtoList = contentsService.findAllByOrderByIdDescPage(pageNumber,size);
        model.addAttribute("contentspage", contentsDtoList);
        return "contents/listContent";
    }
    // 이미지 컨텐츠 컨트롤/////////////////////////////////////////////////////////////////////////////////////////
    //뷰폼
    @GetMapping("/images/{id}")
    public String searchImageById(@PathVariable Long id , Model model) {

        Contents contents = contentsService.searchByIdContent(id);
        FileInfo fileInfo = contentsService.searchByIdFile(contents.getFileId());
        model.addAttribute("contentDto", new ContentsDto(contents));
        model.addAttribute("fileInfo", fileInfo);
        //model.addAttribute("fileList", contentsService.getFileInfoRepository().findAllByContentsType(ContentsType.Image));
        return "contents/images/viewContent";
    }
    //수정폼
    @GetMapping("/images/edit/{id}")
    public String editImageContent(@PathVariable Long id, Model model) {
        // 관리자 or 작성자가 아닐 경우 수정 불가 로직
        Contents contents = contentsService.searchByIdContent(id);
        FileInfo fileInfo = contentsService.searchByIdFile(contents.getFileId());
        model.addAttribute("contentDto", new ContentsDto(contents));
        //model.addAttribute("fileList", contentsService.getFileInfoRepository().findAllByContentsType(ContentsType.Image));
        model.addAttribute("fileInfo", fileInfo);
        return "contents/images/editContent";
    }
    //입력폼
    @GetMapping("/images/edit")
    public String newImageContent(Model model) {
        model.addAttribute("contentDto", new ContentsDto());
        return "contents/images/editContent";
    }

    //리스트
    @GetMapping("/images")
    public String listImageContentPage( @RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
                                        @RequestParam(value = "size", required = false, defaultValue = AppConstants.BoardSizeStr) int size ,
                                        Model model) {
        logger.info("----listcontent 호출");
        //일반
        //model.addAttribute("contents", contentsService.findAllByOrderByIdDescTypeFile(ContentsType.Image));
        //페이징
        Paged<ContentsDto> contentsDtoList = contentsService.findAllByOrderByIdDescTypeFilePage(pageNumber,size,ContentsType.Image);
        model.addAttribute("contentspage", contentsDtoList);
        return "contents/images/listContent";
    }
    // 비디오 컨텐츠 컨트롤//////////////////////////////////////////////////////////////////////////////////////////////
    //뷰
    @GetMapping("/videos/{id}")
    public String searchVideoById(@PathVariable Long id, Model model) {
        Contents contents = contentsService.searchByIdContent(id);
        FileInfo fileInfo = contentsService.searchByIdFile(contents.getFileId());
        model.addAttribute("contentDto", new ContentsDto(contents));
        model.addAttribute("fileInfo", fileInfo);
        return "contents/videos/viewContent";
    }
    //비디오 수정
    @GetMapping("/videos/edit/{id}")
    public String editVideoContent(@PathVariable Long id ,  Model model) {

        Contents contents = contentsService.searchByIdContent(id);
        FileInfo fileInfo = contentsService.searchByIdFile(contents.getFileId());
        model.addAttribute("contentDto", new ContentsDto(contents));
        //model.addAttribute("fileList", contentsService.getFileInfoRepository().findAllByContentsType(ContentsType.Image));
        model.addAttribute("fileInfo", fileInfo);
        return "contents/videos/editContent";
    }
    //비디오 입력폼
    @GetMapping("/videos/edit")
    public String newVideoContent(Model model) {

        model.addAttribute("contentDto", new ContentsDto());

        return "contents/videos/editContent";
    }
    //리스트
    @GetMapping("/videos")
    public String listVideoContentPage( @RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
                               @RequestParam(value = "size", required = false, defaultValue = AppConstants.BoardSizeStr) int size ,
                               Model model) {
        logger.info("----listcontent 호출");
        //페이징
        Paged<ContentsDto> contentsDtoList = contentsService.findAllByOrderByIdDescTypeFilePage(pageNumber,size,ContentsType.Video);
        model.addAttribute("contentspage", contentsDtoList);
        return "contents/videos/listContent";
    }
    // PPT 컨텐츠 컨트롤//////////////////////////////////////////////////////////////////////////////////////////////
    //뷰
    @GetMapping("/ppts/{id}")
    public String searchPptById(@PathVariable Long id, Model model) {
        Contents contents = contentsService.searchByIdContent(id);
        FileInfo fileInfo = contentsService.searchByIdFile(contents.getFileId());
        model.addAttribute("contentDto", new ContentsDto(contents));
        model.addAttribute("fileInfo", fileInfo);
        return "contents/ppts/viewContent";
    }
    //PPT 수정
    @GetMapping("/ppts/edit/{id}")
    public String editPptContent(@PathVariable Long id ,  Model model) {

        Contents contents = contentsService.searchByIdContent(id);
        FileInfo fileInfo = contentsService.searchByIdFile(contents.getFileId());
        model.addAttribute("contentDto", new ContentsDto(contents));
        //model.addAttribute("fileList", contentsService.getFileInfoRepository().findAllByContentsType(ContentsType.Image));
        model.addAttribute("fileInfo", fileInfo);
        return "contents/ppts/editContent";
    }
    //PPT 입력폼
    @GetMapping("/ppts/edit")
    public String newPptContent(Model model) {

        model.addAttribute("contentDto", new ContentsDto());

        return "contents/ppts/editContent";
    }
    //리스트
    @GetMapping("/ppts")
    public String listPptContentPage( @RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
                                        @RequestParam(value = "size", required = false, defaultValue = AppConstants.BoardSizeStr) int size ,
                                        Model model) {
        logger.info("----listcontent 호출");
        //페이징
        Paged<ContentsDto> contentsDtoList = contentsService.findAllByOrderByIdDescTypeFilePage(pageNumber,size,ContentsType.PPT);
        model.addAttribute("contentspage", contentsDtoList);
        return "contents/ppts/listContent";
    }
    ///////////////////////   POPUP   ///////////////////////////////////////
    @GetMapping("/poplist")
    public ModelAndView hardwarelistPagePop(@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
                                            @RequestParam(value = "size", required = false, defaultValue = AppConstants.BoardSizeStr) int size ,
                                            @RequestParam(value = "contype",required = false,defaultValue = "Image") String contype, ModelAndView mav) {
        //type
        Paged<ContentsDto> contentsDtoList = contentsService.findAllByOrderByIdDescTypeFilePage(pageNumber,size,ContentsType.valueOf(contype));
        mav.setViewName("contents/listPopContents :: #result_table_div");
        mav.addObject("contentspage",contentsDtoList);
        mav.addObject("sType",contype);
        return mav;
    }

    ////////////////////////Display Img///////////////////////////////////////////////
    @GetMapping(value = "/display")
    public ResponseEntity<byte[]> displayImgFile(@RequestParam("id") Long id)throws Exception{
        InputStream in = null;
        ResponseEntity<byte[]> entity = null;

        Optional<FileInfo> optAttach = contentsService.getFileInfoRepository().findById(id);

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

    /**
     * 비디오 파일의 섬네일을 찾아서 리턴해준다
     * @param id
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/displayVideo")
    public ResponseEntity<byte[]> displayVideoThumbFile(@RequestParam("id") Long id)throws Exception{
        InputStream in = null;
        ResponseEntity<byte[]> entity = null;

        FileInfo attach = contentsService.getFileInfoRepository().findFileInfoByFileId(id);

        if(attach == null) {
            new RuntimeException("이미지 정보를 찾을 수 없습니다.");
        }

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
