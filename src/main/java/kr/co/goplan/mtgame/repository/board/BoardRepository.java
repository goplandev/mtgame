package kr.co.goplan.mtgame.repository.board;

import kr.co.goplan.mtgame.domain.board.Board;
import kr.co.goplan.mtgame.domain.board.BoardDto;
import kr.co.goplan.mtgame.domain.member.MemberDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

public interface BoardRepository extends JpaRepository<Board , Long> {

    @Query(
            value = "SELECT m FROM Board m WHERE m.isDeleted = false",
            countQuery = "SELECT COUNT (m.id) FROM Board m WHERE m.isDeleted = false"
    )
    Page<BoardDto> findAllDel(Pageable pageable);

    @Query(
            value = "SELECT m FROM Board m WHERE m.isDeleted = false AND m.title LIKE %:keyword%",
            countQuery = "SELECT COUNT (m.id) FROM Board m WHERE m.isDeleted = false AND m.title LIKE %:keyword%"
    )
    Page<BoardDto> findByTitleSearch(@Param("keyword") String keyword, Pageable pageable);


}
