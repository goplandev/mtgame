package kr.co.goplan.mtgame.repository.hardware;

import kr.co.goplan.mtgame.domain.hardware.Hardware;
import kr.co.goplan.mtgame.domain.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HardwareRepository extends JpaRepository<Hardware,Long> {
    @Query(
            value = "SELECT h FROM Hardware h WHERE h.isDelete = 0 AND h.id in (:hwids)",
            countQuery = "SELECT COUNT (h.id) FROM Hardware h WHERE h.isDelete = 0 AND h.id in (:hwids)"
    )
    Page<Hardware> findByIdsSearch(@Param("hwids") List<Long> hwids, Pageable pageable);

    @Query(
            value = "SELECT h FROM Hardware h WHERE h.isDelete = 0",
            countQuery = "SELECT COUNT (h.id) FROM Hardware h WHERE h.isDelete = 0 "
    )
    Page<Hardware> findAllDel(Pageable pageable);

    @Query("SELECT h " + "FROM Hardware h " + "WHERE h.name = :hardware_name")
    Optional<Hardware> findByName(@Param("hardware_name") String hardware_name);
}
