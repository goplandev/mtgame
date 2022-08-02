package kr.co.goplan.mtgame.repository.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.goplan.mtgame.domain.member.Member;
import kr.co.goplan.mtgame.domain.member.MemberDto;
import kr.co.goplan.mtgame.domain.member.MemberSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member , Long> , MemberRepositoryCustom{

    Optional<Member> findByName(String username);
    Optional<Member> findByEmail(String email);
    Page<MemberDto> findByIsDelete(int n,Pageable pageable);
    Page<MemberDto> findByEmailContaining(@Param("keyword") String keyword, Pageable pageable);
    /*@PersistenceContext
    private EntityManager em;

    public void save(Member member){

        if(member.getId() == null){
            em.persist(member);
        }else{
            em.merge(member);
        }
    }*/
    /*@Query(
            value = "SELECT m FROM Member m WHERE m.isDelete = 0 AND m.email LIKE %:keyword%",
            countQuery = "SELECT COUNT (m.id) FROM Member m WHERE m.isDelete = 0 AND m.email LIKE %:keyword%"
    )
    Page<MemberDto> findByEmailSearch(@Param("keyword") String keyword, Pageable pageable);

    @Query(
            value = "SELECT m FROM Member m WHERE m.isDelete = 0",
            countQuery = "SELECT COUNT (m.id) FROM Member m WHERE m.isDelete = 0"
    )
    Page<MemberDto> findAllDel(Pageable pageable);

    List<Member> findByEmailContaining(String keyword);

    @Query("SELECT m " + "FROM Member m " + "WHERE m.name = :name")
    Optional<Member> findByName(String username);
    Optional<Member> findByemail(String email);*/

}

