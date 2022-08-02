package kr.co.goplan.mtgame.repository.contents;

import kr.co.goplan.mtgame.constant.ContentsType;
import kr.co.goplan.mtgame.domain.contents.Contents;
import kr.co.goplan.mtgame.domain.contents.ContentsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface ContentsRepository extends JpaRepository<Contents, Long> {

    List<Contents> findAllByOrderByIdDesc();

    @Query("select o from Contents as o where o.contentType = :contentType ")
    List<Contents> findAllByOrderByIdDescType(@Param("contentType") ContentsType contentType);

    @Query("select distinct o from Contents o " +
            "join fetch o.fileInfo f " +
            "where o.contentType = :contentType ")
    List<Contents> findAllByOrderByIdDescTypeFile(@Param("contentType") ContentsType contentType);

    /*@Query(value = "select distinct o from Contents o join fetch o.fileInfo f where o.contentType = :contentType ",
            countQuery = "SELECT DISTINCT COUNT (o.id) FROM Contents o join fetch o.fileInfo f where o.contentType = :contentType "
    )


    Page<ContentsDto> findAllByOrderByIdDescTypeFilePage(@Param("contentType") ContentsType contentType , Pageable pageable);*/
    @Query(value = "select distinct o from Contents o where o.contentType = :contentType ",
            countQuery = "SELECT DISTINCT COUNT (o.id) FROM Contents o where o.contentType = :contentType "
    )
    Page<ContentsDto> findAllByOrderByIdDescTypeFilePage(@Param("contentType") ContentsType contentType , Pageable pageable);

    @Query(value = "select distinct o from Contents o ",
            countQuery = "SELECT DISTINCT COUNT (o.id) FROM Contents o  "
    )
    Page<ContentsDto> findAllByOrderByIdDescPage( Pageable pageable);

}
