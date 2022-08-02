package kr.co.goplan.mtgame.service.member;

import kr.co.goplan.mtgame.domain.member.Member;
import kr.co.goplan.mtgame.domain.member.MemberDto;
import kr.co.goplan.mtgame.domain.member.Role;
import kr.co.goplan.mtgame.repository.member.MemberRepository;
import kr.co.goplan.mtgame.util.paging.Paged;
import kr.co.goplan.mtgame.util.paging.Paging;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
@Getter
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원가입
     */
    @Transactional //변경
    public Long save(MemberDto memberDto) {
        validateDuplicateMember(memberDto); //중복 회원 검증
        memberRepository.save(memberDto.toEntity());
        return memberDto.getId();
    }
    @Transactional //변경
    public Long edit(MemberDto memberDto) {
        memberRepository.save(memberDto.toEntity());
        return memberDto.getId();
    }
    public MemberDto findMemberOne(Long id){
        //return memberRepository.findOne(id);
        //JpaRepository 사용시
        MemberDto memberDto = new MemberDto(memberRepository.findById(id).get());
        return memberDto;
    }
    public MemberDto findEmail(String email){
        Optional<Member> memberWrapper = memberRepository.findByEmail(email);
        MemberDto memberDto = null;
        if(!memberWrapper.isEmpty()){
            memberDto = new MemberDto(memberWrapper.get());
        }
        return memberDto;
    }
    public Paged<MemberDto> memberSearchPage(int pageNumber, int size , String keyword) {
        PageRequest request = PageRequest.of(pageNumber -1 , size , Sort.Direction.DESC,"id");
        Page<MemberDto> memberList;
        if(keyword.equals("")){
            //memberList = memberDAO.findAll(request);
            //memberList = memberRepository.findAllDel(request);
            memberList = memberRepository.findByIsDelete(0,request);
        }else{
            memberList = memberRepository.findByEmailContaining(keyword,request);
        }
        return new Paged<>(memberList, Paging.of(memberList.getTotalPages(), pageNumber, size));
    }
    // 회원가입
    @Transactional
    public Long signUp(MemberDto memberDto) {
        //BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        //memberDto.setPassword(passwordEncoder.encode(memberDto.getPassword()));

        // password를 암호화 한 뒤 dp에 저장

        return memberRepository.save(memberDto.toEntity()).getId();
    }

    // 회원가입 벨리데이션 체크
    private void validateDuplicateMember(MemberDto memberDto) {
        Optional<Member> memberWrapper = memberRepository.findByEmail(memberDto.getEmail());

        if(!memberWrapper.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    public Member searchByName(String name){
        //Member member = memberRepository.findByName(name).orElseThrow(()-> new IllegalArgumentException("존재하지 않는 회원입니다."));
        Member member = memberRepository.findByName(name).orElseThrow(()->new IllegalArgumentException("존재하지 않는 회원입니다."));
        return member;
    }

}
