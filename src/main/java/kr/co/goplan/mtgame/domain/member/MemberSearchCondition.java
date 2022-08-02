package kr.co.goplan.mtgame.domain.member;

import lombok.Data;

@Data
public class MemberSearchCondition {
    //회원명, 팀명, 나이(ageGoe, ageLoe)
    private String username;
    private String email;
    private String groupName;
    private Integer ageGoe;
    private Integer ageLoe;
}
