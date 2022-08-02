package kr.co.goplan.mtgame.repository.schedule;

import kr.co.goplan.mtgame.domain.board.BoardDto;
import kr.co.goplan.mtgame.domain.schedule.ScheduleInfo;
import kr.co.goplan.mtgame.domain.schedule.ScheduleInfoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<ScheduleInfo , Long> {

    /*@Query(
            value = "SELECT m FROM ScheduleInfo m WHERE m.isDeleted = false",
            countQuery = "SELECT COUNT (m.id) FROM ScheduleInfo m WHERE m.isDeleted = false"
    )
    Page<ScheduleInfoDto> findAllDel(Pageable pageable);*/
    /* 안됨 페이징에서 패치조인 문제 해결해야함*/
    @Query(
            value = "SELECT distinct si FROM ScheduleInfo si " +
                    /*"join fetch si.register r " +
                    "join fetch si.modifier m " +*/
                    "WHERE si.isDeleted = false",
            countQuery = "SELECT distinct COUNT (si.id) FROM ScheduleInfo si "+
                    "WHERE si.isDeleted = false"
    )
    Page<ScheduleInfoDto> findAllDel(Pageable pageable);


    @Query(
            value = "SELECT m FROM ScheduleInfo m WHERE m.isDeleted = false AND m.title LIKE %:keyword%",
            countQuery = "SELECT COUNT (m.id) FROM ScheduleInfo m WHERE m.isDeleted = false AND m.title LIKE %:keyword%"
    )
    Page<ScheduleInfoDto> findByTitleSearch(String keyword, Pageable pageable);
    
    @Query(
            value = "SELECT distinct si FROM ScheduleInfo si " +
                    /*"join fetch si.register r " +
                    "join fetch si.modifier m " +*/
                    "join fetch si.scheduleContentMappings s " +
                    "join fetch s.contents c " +
                    "WHERE si.isDeleted = false AND si.id = (:id) AND s.isDeleted = false AND c.isDeleted = false "
    )
    List<ScheduleInfo> findScheduleMapId(@Param("id")Long id);
}
