package kr.co.goplan.mtgame.repository.app;

import kr.co.goplan.mtgame.domain.app.AppInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppRepository extends JpaRepository<AppInfo , Long> {
}
