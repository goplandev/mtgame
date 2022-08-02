package kr.co.goplan.mtgame.domain.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
public class MemberDto {

    private Long id = null;

    @NotEmpty(message = "이메일은 필수 입니다")
    private String email;

    @NotEmpty(message = "비밀 번호는 필수 입니다")
    private String password;

    private String newpassword;

    private String name;

    private String phone;

    private Role role;

    private int isDelete;

    private String createdBy;
    private String lastModifiedBy;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    public Member toEntity() {
        return Member.builder()
                .id(id)
                .name(name)
                .email(email)
                .password(password)
                .phone(phone)
                .role(role)
                .isDelete(isDelete)
                .build();
    }
    @Builder
    public MemberDto(Member member) {
        this.id = member.getId();
        this.name = member.getName();
        this.email = member.getEmail();
        this.password = member.getPassword();
        this.phone = member.getPhone();
        this.role = member.getRole();
        this.createdBy = member.getCreatedBy();
        this.lastModifiedBy = member.getLastModifiedBy();
        this.createdDate = member.getCreatedDate();
        this.lastModifiedDate = member.getLastModifiedDate();
        this.isDelete = member.getIsDelete();
    }
}
