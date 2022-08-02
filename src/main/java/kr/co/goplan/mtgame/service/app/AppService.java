package kr.co.goplan.mtgame.service.app;

import kr.co.goplan.mtgame.domain.app.AppInfo;
import kr.co.goplan.mtgame.domain.board.BoardDto;
import kr.co.goplan.mtgame.repository.app.AppRepository;
import kr.co.goplan.mtgame.util.FileUtilities;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Getter
@Setter
public class AppService {
    private final AppRepository appRepository;

    @Transactional //변경
    public Long save(AppInfo appInfo) {
        appRepository.save(appInfo);

        return appInfo.getId();
    }

    public List<AppInfo> appInfoList(){
        return appRepository.findAll();
    }
}
