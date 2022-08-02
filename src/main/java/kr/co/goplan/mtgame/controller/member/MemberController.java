package kr.co.goplan.mtgame.controller.member;

import kr.co.goplan.mtgame.constant.AppConstants;
import kr.co.goplan.mtgame.domain.member.MemberDto;
import kr.co.goplan.mtgame.domain.member.Role;
import kr.co.goplan.mtgame.service.member.MemberService;
import kr.co.goplan.mtgame.util.paging.Paged;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@AllArgsConstructor
public class MemberController {

    private final MemberService memberService;

    //회원가입폼
    @GetMapping("/members/new")
    public String cerateForm(Model model){
        model.addAttribute("memberDto",new MemberDto());
        return "members/editMember";
    }
    //회원가입
    @PostMapping("/members/new")
    public String create(@Valid MemberDto dto, BindingResult result){

        /*if(result.hasErrors()){
            return "members/editMember";
        }*/
        if(dto.getId() != null){
            //수정
            if(dto.getNewpassword() != ""){
                dto.setPassword(dto.getNewpassword());
            }else{
                MemberDto memberDto2 = memberService.findMemberOne(dto.getId());
                dto.setPassword(memberDto2.getPassword());
            }
            memberService.edit(dto);
        }else{
            //등록
            dto.setRole(Role.MEMBER);
            memberService.save(dto);

        }
        /*Member member = new Member();
        member.setName(dto.getName());
        member.setEmail(dto.getEmail());
        //BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        //member.setPassword(passwordEncoder.encode(dto.getPassword()));
        member.setPassword(dto.getPassword());
        member.setPhone(dto.getPhone());*/
        //dto.setRole(Role.MEMBER);
        //dto.setModifiedDatetime(LocalDateTime.now());
        //dto.setRegisteredDatetime(LocalDateTime.now());
        //memberService.save(dto);

        return "redirect:/members";
    }
    //회원수정폼
    @GetMapping("/members/{id}/edit")
    public String editForm(@PathVariable("id") Long id , Model model){
        MemberDto memberDto = memberService.findMemberOne(id);
        model.addAttribute("memberDto" , memberDto);
        return "members/editMember";

    }
    //회원수정
    @PostMapping("/members/{id}/edit")
    public String edit(@ModelAttribute("memberDto") MemberDto memberDto){
       /* Member member1 = new Member();
        member1.setId(member.getId());
        member1.setName(member.getName());
        member1.setEmail(member.getEmail());
        //member1.setPassword(member.getPassword());
        member1.setPhone(member.getPhone());
        member1.setSns(member.getSns());
*/
       // memberDto.setModifiedDatetime(LocalDateTime.now());
        if(memberDto.getNewpassword() != ""){
            memberDto.setPassword(memberDto.getNewpassword());
        }else{
            MemberDto memberDto2 = memberService.findMemberOne(memberDto.getId());
            memberDto.setPassword(memberDto2.getPassword());
        }
        memberService.edit(memberDto);

        return "redirect:/members";

    }
    //페이징 및 페이징 검색
    @GetMapping(value = "/members")
    public String listPage(@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
                           @RequestParam(value = "size", required = false, defaultValue = AppConstants.BoardSizeStr) int size ,
                           @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword ,Model model) {

        //페이징만
        //Paged<Member> memberpages = memberService.memberListPage(pageNumber,size);

        //페이지 검색
        Paged<MemberDto> memberpages = memberService.memberSearchPage(pageNumber,size,keyword);
        List<MemberDto> memberList = memberpages.getPage().getContent();
        model.addAttribute("memberpages", memberpages);
        model.addAttribute("members", memberList);
        return "members/listMember";
    }
}
