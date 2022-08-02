package kr.co.goplan.mtgame.controller.board;

import kr.co.goplan.mtgame.constant.AppConstants;
import kr.co.goplan.mtgame.domain.board.BoardDto;
import kr.co.goplan.mtgame.service.board.BoardService;
import kr.co.goplan.mtgame.util.paging.Paged;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@AllArgsConstructor
public class BoardController {

    private final BoardService boardService;
    //뷰
    @GetMapping("/boards/{id}")
    public String searchById(@PathVariable Long id, Model model) {
        model.addAttribute("boardDto", boardService.findOne(id));
        model.addAttribute("fileList", boardService.getFileInfoRepository().findAllByBoardId(id));
        return "boards/viewBoard";
    }
    //글작성폼
    @GetMapping("/boards/new")
    public String cerateForm(Model model){
        model.addAttribute("boardDto",new BoardDto());
        return "boards/editBoard";
    }
    //글작성
    /*@PostMapping("/boards/new")
    public String create(@Valid BoardDto dto, BindingResult result){

        if(result.hasErrors()){
            return "boards/createBoard";
        }

        dto.setModifiedDatetime(LocalDateTime.now());
        dto.setRegisteredDatetime(LocalDateTime.now());
        boardService.save(dto);

        return "redirect:/boards";
    }*/
    //글수정폼
    @GetMapping("/boards/edit/{id}")
    public String editForm(@PathVariable("id") Long id , Model model){
        BoardDto boardDto = boardService.findOne(id);
        model.addAttribute("boardDto" , boardDto);
        model.addAttribute("fileList", boardService.getFileInfoRepository().findAllByBoardId(id));
        return "boards/editBoard";
    }
    //글수정
    /*@PostMapping("/boards/{id}/edit")
    public String edit(@ModelAttribute("boardDto") BoardDto boardDto){

        boardDto.setModifiedDatetime(LocalDateTime.now());
        boardService.edit(boardDto);

        return "redirect:/boards";
    }*/
    //페이징 및 페이징 검색
    @GetMapping(value = "/boards")
    public String listPage(@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
                           @RequestParam(value = "size", required = false, defaultValue = AppConstants.BoardSizeStr) int size ,
                           @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword ,Model model) {

        //페이징만
        //Paged<Member> memberpages = memberService.memberListPage(pageNumber,size);

        //페이지 검색
        Paged<BoardDto> boardpages = boardService.boardSearchPage(pageNumber,size,keyword);
        List<BoardDto> boardList = boardpages.getPage().getContent();
        model.addAttribute("boardpages", boardpages);
        model.addAttribute("boards", boardList);
        return "boards/listBoard";
    }
}
