package kr.co.goplan.mtgame.api.member;

import kr.co.goplan.mtgame.domain.member.Member;
import kr.co.goplan.mtgame.domain.member.MemberDto;
import kr.co.goplan.mtgame.service.member.MemberService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequiredArgsConstructor
public class MemberApiController {
    private final MemberService memberService;

    @GetMapping("/api/member_delete")
    public Long deleteMember(@RequestParam(value = "member_id", required = false, defaultValue = "0") Long member_id , ModelAndView mav){
        //맵핑된 장비 제거
        MemberDto memberDto = memberService.findMemberOne(member_id);
        memberDto.setIsDelete(1);
        memberService.edit(memberDto);
        return 1l;
    }
    @GetMapping("/api/member_email_chack")
    public MemberResponse chackMemberEmail(@RequestParam(value = "email", required = false, defaultValue = "0") String email , ModelAndView mav){
        //이름 확인
        Boolean ischack = false;//사용가능
        MemberDto memberDto = memberService.findEmail(email);

        if(memberDto != null){
            ischack = true;//사용못함
        }
        return new MemberResponse(ischack);
    }
    @Data
    static class MemberResponse{
        private Boolean ischack;
        public MemberResponse(Boolean ischack){
            this.ischack = ischack;
        }
    }
}
