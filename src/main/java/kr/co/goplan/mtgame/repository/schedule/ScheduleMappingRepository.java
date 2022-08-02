package kr.co.goplan.mtgame.repository.schedule;

import kr.co.goplan.mtgame.domain.schedule.ScheduleContentMapping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleMappingRepository extends JpaRepository<ScheduleContentMapping,Long> {

}
