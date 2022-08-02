package kr.co.goplan.mtgame.api.hardware;

import kr.co.goplan.mtgame.domain.hardware.Hardware;
import kr.co.goplan.mtgame.domain.hardware.HardwareDto;
import kr.co.goplan.mtgame.service.hardware.HardWareService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequiredArgsConstructor
public class HardwardApiController {
    private final HardWareService hardWareService;

    //private final HardWareGroupService hardWareGroupService;

    /*@GetMapping("/api/hardware_map_delete")
    public Long deleteMapping(@RequestParam(value = "hardware_map_id", required = false, defaultValue = "0") Long hardware_map_id , ModelAndView mav){
        //맵핑된 스케쥴 제거
        return hardWareGroupService.deleteHardwareMapping(hardware_map_id);
    }*/
    @GetMapping("/api/hardware_delete")
    public Long deleteHardware(@RequestParam(value = "hardware_id", required = false, defaultValue = "0") Long hardware_id , ModelAndView mav){
        //장비 제거
        Hardware hardware = hardWareService.findOne(hardware_id);
        hardware.setIsDelete(1);
        hardWareService.saveHardWare(hardware);
        return 1l;
    }
    @GetMapping("/api/hardware_name_chack")
    public HardwareResponse chackHarewareName(@RequestParam(value = "hardware_name", required = false, defaultValue = "0") String hardware_name , ModelAndView mav){
        //이름 확인
        Boolean ischack = false;//사용가능
        HardwareDto hardwareDto = hardWareService.findOneName(hardware_name);

        if(hardwareDto != null){
            ischack = true;//사용못함
        }
        return new HardwareResponse(ischack);
    }
    @Data
    static class HardwareResponse{
        private Boolean ischack;
        public HardwareResponse(Boolean ischack){
            this.ischack = ischack;
        }
    }
}
