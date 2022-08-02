package kr.co.goplan.mtgame.repository.hardware;

import kr.co.goplan.mtgame.domain.hardware.HardwareGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HardwareGroupRepository extends JpaRepository<HardwareGroup,Long> {
    @Query(
            value = "SELECT h FROM HardwareGroup h WHERE h.isDelete = 0 AND h.id in (:hwgids)",
            countQuery = "SELECT COUNT (h.id) FROM HardwareGroup h WHERE h.isDelete = 0 AND h.id = (:hwgids)"
    )
    Page<HardwareGroup> findByIdSearch(@Param("hwgids") List<Long> hwgids, Pageable pageable);

    @Query(
            value = "SELECT h FROM HardwareGroup h WHERE h.isDelete = 0",
            countQuery = "SELECT COUNT (h.id) FROM HardwareGroup h WHERE h.isDelete = 0 "
    )
    Page<HardwareGroup> findAllDel(Pageable pageable);

    @Query(
            value = "SELECT distinct h from HardwareGroup h " +
                    "join fetch h.hardwareMaps hm " +
                    "join fetch hm.hardware where h.isDelete = 0"
    )
    List<HardwareGroup> findAllWithItem();

    @Query(
            value = "SELECT distinct h from HardwareGroup h " +
                    "join fetch h.hardwareMaps hm " +
                    "join fetch hm.hardware where h.isDelete = 0 and h.id = :id"
    )
    List<HardwareGroup> findAllHardware(@Param("id") Long id);

}
