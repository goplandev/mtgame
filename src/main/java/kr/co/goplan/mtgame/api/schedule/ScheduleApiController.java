package kr.co.goplan.mtgame.api.schedule;

import kr.co.goplan.mtgame.service.schedule.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class ScheduleApiController {

    private final ScheduleService scheduleService;

    ///////////   contents mapping ////
    // ///////-----------------------//////////////

    @GetMapping("/schedule_contents_map_delete")
    public Long deleteMapping(@RequestParam(value = "contents_map_id", required = false, defaultValue = "0") Long contents_map_id , ModelAndView mav){
        //맵핑된 스케쥴 제거
        scheduleService.deleteContentsMapping(contents_map_id);
        return 1l;
    }
}
