package kr.co.goplan.mtgame.controller.hardware;

import kr.co.goplan.mtgame.constant.AppConstants;
import kr.co.goplan.mtgame.domain.hardware.Hardware;
import kr.co.goplan.mtgame.repository.hardware.HardwareRepository;
import kr.co.goplan.mtgame.service.hardware.HardWareService;
import kr.co.goplan.mtgame.util.paging.Paged;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HardwareController {
    private final HardWareService hardWareService;
    private final HardwareRepository hardwareRepository;

    @GetMapping("/hardwares/new")
    public ModelAndView createForm(ModelAndView mav){
        mav.setViewName("hardwares/editHardware");
        mav.addObject("hardware" , new Hardware());
        return mav;
    }

    @PostMapping("/hardwares/new")
    public ModelAndView create(@Valid Hardware hardware, BindingResult result){

        if(result.hasErrors()){
            return new ModelAndView("hardwares/createHardware");
        }
        if(hardware.getId() == null){
            //신규등록
            Hardware hardware1 = new Hardware();
            hardware1.setName(hardware.getName());
            hardware1.setIp(hardware.getIp());
            hardware1.setMac(hardware.getMac());
            hardware1.setDescription(hardware.getDescription());
            hardWareService.saveHardWare(hardware1);
        }else{
            //수정
            hardWareService.saveHardWare(hardware);
        }


        return new ModelAndView("redirect:/hardwares");
    }

    @GetMapping(value = "/hardwares")
    public ModelAndView listPage(@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
                                 @RequestParam(value = "size", required = false, defaultValue = AppConstants.BoardSizeStr) int size , ModelAndView mav) {

        /*List<GrantedAuthority> authorities = (List<GrantedAuthority>) authentication.getAuthorities();
        String strAuth = authorities.get(0).getAuthority();*/

        //요청자가 어드민일경우
        Paged<Hardware> hardwares = hardWareService.hardwareListPage(pageNumber,size);
        mav.setViewName("hardwares/listHardware");
        mav.addObject("hardwares",hardwares);


        return mav;
    }
    @GetMapping("/hardwares/edit/{hardware_id}")
    public String editForm(@PathVariable("hardware_id") Long hardware_id, Model model){

        Hardware hardware = (Hardware) hardWareService.findOne(hardware_id);
        hardware.setScids("");
        model.addAttribute("hardware",hardware);

        //맵핑된 스케쥴
        //List<Hardware> mappings2 = scheduleService.findAllSchedule(hardware_id);
        //dto 사용시
        //List<HardwareDto> mappings = mappings2.stream().map(o ->new HardwareDto(o)).collect(toList());

        //model.addAttribute("mappings", mappings2);

        //List<Info> infos;
        //infos = infoService.findLast(hardware_id);
        //model.addAttribute("infos",infos);

        //return "hardwares/editHardwareInfo";
        return "hardwares/editHardware";
    }
    @GetMapping("/hardwares/view/{hardware_id}")
    public String viewForm(@PathVariable("hardware_id") Long hardware_id, Model model){

        Hardware hardware = (Hardware) hardWareService.findOne(hardware_id);

        model.addAttribute("hardware",hardware);

        //맵핑된 스케쥴
        //List<Hardware> mappings2 = scheduleService.findAllSchedule(hardware_id);
        //model.addAttribute("mappings", mappings2);

        //List<Info> infos;
        //infos = infoService.findLast(hardware_id);
        //model.addAttribute("infos",infos);
        return "hardwares/viewHardware";
    }

    /**
     *
     * @param pageNumber
     * @param size
     * @param mav
     * @return
     */
    @GetMapping("/modals/hardwares/poplist")
    public ModelAndView hardwarelistPagePop(@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
                                            @RequestParam(value = "size", required = false, defaultValue = AppConstants.BoardSizeStr) int size , ModelAndView mav) {

        Paged<Hardware> hardwares = hardWareService.hardwareListPage(pageNumber,size);
        mav.setViewName("hardwares/popHardwarelist :: #result_table_div");
        mav.addObject("hardwares",hardwares);

        return mav;
    }
}
