package kr.co.goplan.mtgame.api.hardware;

import kr.co.goplan.mtgame.domain.hardware.HardwareGroup;
import kr.co.goplan.mtgame.service.hardware.HardWareGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequiredArgsConstructor
public class HardwareGroupApoController {
    private final HardWareGroupService hardWareGroupService;

    @GetMapping("/api/hardware_group_delete")
    public Long deleteHardwareGroup(@RequestParam(value = "group_id", required = false, defaultValue = "0") Long group_id , ModelAndView mav){
        //장비그룹 제거
        HardwareGroup hardwareGroup = hardWareGroupService.findOne(group_id);
        hardwareGroup.setIsDelete(1);
        hardWareGroupService.saveHardWareGroup(hardwareGroup);
        return 1l;
    }
}
