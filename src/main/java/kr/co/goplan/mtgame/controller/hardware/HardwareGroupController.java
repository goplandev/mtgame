package kr.co.goplan.mtgame.controller.hardware;

import kr.co.goplan.mtgame.constant.AppConstants;
import kr.co.goplan.mtgame.domain.MySearch;
import kr.co.goplan.mtgame.domain.hardware.Hardware;
import kr.co.goplan.mtgame.domain.hardware.HardwareGroup;
import kr.co.goplan.mtgame.service.hardware.HardWareGroupService;
import kr.co.goplan.mtgame.service.hardware.HardWareService;
import kr.co.goplan.mtgame.service.member.MemberService;
import kr.co.goplan.mtgame.util.paging.Paged;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Controller
@RequiredArgsConstructor
public class HardwareGroupController {
    private final HardWareGroupService hardWareGroupService;
    private final HardWareService hardWareService;
    //하드웨어 그룹생성 뷰
    @GetMapping("/hardwares/newgroup")
    public String createForm(Model model){
        model.addAttribute("hardwareGroup", new HardwareGroup());
        return "hardwares/editHardwareGroup";
    }

    //하드웨어 그룹생성
    @PostMapping("/hardwares/newgroup")
    public String create(@Valid HardwareGroup hardwareGroup, BindingResult result){

        if(result.hasErrors()){
            return "hardwares/editHardwareGroup";
        }
        if(hardwareGroup.getId() == null) {
            //생성
            HardwareGroup hardwareGroup1 = new HardwareGroup();
            hardwareGroup1.setName(hardwareGroup.getName());
            hardwareGroup1.setDescription(hardwareGroup.getDescription());
            hardWareGroupService.saveHardWareGroup(hardwareGroup1);
        }else{
            //수정
            hardWareGroupService.saveHardWareGroup(hardwareGroup);
        }

        return "redirect:/hardwaregroups";
    }

    @GetMapping("/hardwares/mapping")
    public String createMappingForm(Model model){

        List<Hardware> hardwares = hardWareService.hardwareList();
        model.addAttribute("hardwares", hardwares);
        return "hardwares/mappingHardware";
    }
    @PostMapping(value = "/hardwares/mapping")
    public String createMapping(@RequestParam("memberId") Long memberId,
                                @RequestParam("hardwareId") Long hardwareId)
    {
        Long str = hardWareGroupService.saveHardwareMap(memberId, hardwareId);

        return "redirect:/hardwares/mappings";
    }
    @GetMapping(value = "/hardwaregroups")
    public String listPage(@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
                           @RequestParam(value = "size", required = false, defaultValue = AppConstants.BoardSizeStr) int size , Model model)
    {
        /*List<GrantedAuthority> authorities = (List<GrantedAuthority>) authentication.getAuthorities();
        String strAuth = authorities.get(0).getAuthority();*/

        Paged<HardwareGroup> hardwareGroups = hardWareGroupService.hardwareGroupListPage(pageNumber, size);
        model.addAttribute("hardwareGroups", hardwareGroups);

        return "hardwares/listHardwareGroup";
    }

    @GetMapping(value = "/hardwares/mappings")
    public String listMapping(@ModelAttribute("mappingSearch") MySearch
                                      mySearch, Model model) {
        List<HardwareGroup> mappings2 = hardWareGroupService.findAllWithItem();
        //List<HardwareGroupDto> mappings = mappings2.stream().map(o ->new HardwareGroupDto(o)).collect(toList());
        model.addAttribute("mappings", mappings2);
        return "hardwares/listMappingHardware";
    }

    //하드웨어 그룹수정 뷰
    @GetMapping("/hardwares/editgroup/{id}")
    public String editForm(@PathVariable("id") Long id , Model model){

        HardwareGroup hardwareGroup = hardWareGroupService.findOne(id);
        hardwareGroup.setHwids("");
        model.addAttribute("hardwareGroup" , hardwareGroup);

        //맵핑된 하드웨어
        List<HardwareGroup> mappings2 = hardWareGroupService.findAllHardware(id);
        //List<HardwareGroupDto> mappings = mappings2.stream().map(o ->new HardwareGroupDto(o)).collect(toList());

        model.addAttribute("mappings", mappings2);

        return "hardwares/editHardwareGroup";
    }
    @PostMapping("/hardwares/editgroup/{id}")
    public String edit(@ModelAttribute("hardwareGroup") HardwareGroup hardwareGroup , @RequestParam(value = "hwids" , defaultValue = "0") String hwids){

        String[] shwidsArr = hwids.split(",");

        if(shwidsArr.length > 0) {
            hardWareGroupService.editHardwareMap(hardwareGroup, shwidsArr);
        }else{
            hardWareGroupService.editHardwareMap(hardwareGroup);
        }
        return "redirect:/hardwaregroups";
    }
}
