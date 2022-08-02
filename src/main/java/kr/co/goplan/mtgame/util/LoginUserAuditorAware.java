package kr.co.goplan.mtgame.util;

import kr.co.goplan.mtgame.domain.member.Member;
import kr.co.goplan.mtgame.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LoginUserAuditorAware implements AuditorAware<String> {
    private final MemberService memberService;
    @Override
    public Optional<String> getCurrentAuditor() {
        /*Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (null == authentication || !authentication.isAuthenticated()) {
            return null;
        }
        User user = (User) authentication.getPrincipal();
        return Optional.of(user.getUserId());
        */
        Member member = memberService.findMemberOne(1l).toEntity();
        return Optional.of(member.getName());
    }


}
