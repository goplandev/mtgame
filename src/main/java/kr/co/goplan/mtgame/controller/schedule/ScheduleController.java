package kr.co.goplan.mtgame.controller.schedule;

import com.sun.xml.bind.v2.schemagen.xmlschema.Appinfo;
import kr.co.goplan.mtgame.api.board.BoardApiController;
import kr.co.goplan.mtgame.constant.AppConstants;
import kr.co.goplan.mtgame.domain.app.AppInfo;
import kr.co.goplan.mtgame.domain.board.BoardDto;
import kr.co.goplan.mtgame.domain.member.Member;
import kr.co.goplan.mtgame.domain.member.MemberDto;
import kr.co.goplan.mtgame.domain.member.Role;
import kr.co.goplan.mtgame.domain.schedule.ScheduleInfo;
import kr.co.goplan.mtgame.domain.schedule.ScheduleInfoDto;
import kr.co.goplan.mtgame.repository.schedule.ScheduleRepository;
import kr.co.goplan.mtgame.service.app.AppService;
import kr.co.goplan.mtgame.service.member.MemberService;
import kr.co.goplan.mtgame.service.schedule.ScheduleService;
import kr.co.goplan.mtgame.util.paging.Paged;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class ScheduleController {

    private static final Logger logger = LoggerFactory.getLogger(BoardApiController.class);
    private final ScheduleService scheduleService;
    private final MemberService memberService;
    private final ScheduleRepository scheduleRepository;
    private final AppService appService;

    //뷰
    @GetMapping("/schedules/{id}")
    public String searchById(@PathVariable Long id, Model model) {

        //스케쥴
        ScheduleInfoDto scheduleInfoDto = scheduleService.findOne(id);
        //맵핑된 컨텐츠
        if(scheduleInfoDto.getScheduleContentMappingDtos().size()>0) {
            List<ScheduleInfo> scheduleInfoList = scheduleService.findScheduleMapId(id);
            List<ScheduleInfoDto> scheduleInfoDtoList = scheduleInfoList.stream().map(o -> new ScheduleInfoDto(o)).collect(Collectors.toList());
            model.addAttribute("scheduleInfoDtoMap", scheduleInfoDtoList);
        }
        model.addAttribute("scheduleInfoDto", scheduleInfoDto);

        return "schedules/viewSchedule";
    }
    //스케쥴등록 폼
    @GetMapping("/schedules/edit")
    public String cerateForm(Model model) {
        model.addAttribute("scheduleInfoDto", new ScheduleInfoDto());
        return "schedules/editSchedule";
    }
    //스케쥴 수정폼
    @GetMapping("/schedules/edit/{id}")
    public String editForm(@PathVariable("id") Long id , Model model){
        //스케쥴
        ScheduleInfoDto scheduleInfoDto = scheduleService.findOne(id);
        //맵핑된 컨텐츠
        if(scheduleInfoDto.getScheduleContentMappingDtos().size()>0) {
            List<ScheduleInfo> scheduleInfoList = scheduleService.findScheduleMapId(id);
            List<ScheduleInfoDto> scheduleInfoDtoList = scheduleInfoList.stream().map(o -> new ScheduleInfoDto(o)).collect(Collectors.toList());
            model.addAttribute("scheduleInfoDtoMap", scheduleInfoDtoList);
        }
        model.addAttribute("scheduleInfoDto", scheduleInfoDto);
        return "schedules/editSchedule";
    }

    //스케쥴 등록
    @PostMapping("/schedules/new")
    public String create(@Valid ScheduleInfoDto dto, BindingResult result){
        if(result.hasErrors()){
            return "schedules/editSchedule";
        }
        Member member = memberService.findMemberOne(1l).toEntity();
        String[] shwidsArr = dto.getScids().split(",");

        if (dto.getId() == null) {
            // 신규 등록

            /*dto.setRegister(member);
            dto.setModifier(member);
            dto.setModifiedDatetime(LocalDateTime.now());
            dto.setRegisteredDatetime(LocalDateTime.now());*/

            if (shwidsArr[0] != "") {
                scheduleService.editScheduleMap(dto.toEntity(), shwidsArr);
            } else {
                scheduleService.editScheduleMap(dto.toEntity());
            }
        }else{
            // 수정
            /*ScheduleInfoDto sdto = scheduleService.findOne(dto.getId());
            dto.setRegister(sdto.getRegister());
            dto.setModifier(member);
            dto.setModifiedDatetime(LocalDateTime.now());*/
            //if (shwidsArr.length > 0) {
            if (shwidsArr[0] != "") {
                scheduleService.editScheduleMap(dto.toEntity(), shwidsArr);
            } else {
                scheduleService.editScheduleMap(dto.toEntity());
            }
        }
        //scheduleService.save(dto);
        return "redirect:/schedules";
    }
    //페이징 및 페이징 검색
    @GetMapping(value = "/schedules")
    public String listPage(@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
                           @RequestParam(value = "size", required = false, defaultValue = AppConstants.BoardSizeStr) int size ,
                           @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword ,Model model) {

        //페이징만
        //Paged<Member> memberpages = memberService.memberListPage(pageNumber,size);

        //페이지 검색
        Paged<ScheduleInfoDto> schedulepages = scheduleService.scheduleSearchPage(pageNumber,size,keyword);
        List<ScheduleInfoDto> schedules = schedulepages.getPage().getContent();
        model.addAttribute("schedulepages", schedulepages);
        model.addAttribute("schedules", schedules);
        return "schedules/listSchedule";
    }

/*
    //@ModelAttribute("weekdays")
    private Map<String,Boolean> weekdaymap(){
        Map<String,Boolean> map = new LinkedHashMap<String,Boolean>();
        map.put("Monday",false);
        map.put("Tuesday",false);
        map.put("Wednesday",false);
        map.put("Thursday",false);
        map.put("Friday",false);
        map.put("Saturday",false);
        map.put("Sunday",false);
        return map;
    }
    //@ModelAttribute("weekdays")
    private Map<String,Boolean> weekdaymap(Boolean m ,Boolean t,Boolean w,Boolean th,Boolean f,Boolean s,Boolean su ){
        Map<String,Boolean> map = new LinkedHashMap<String,Boolean>();
        map.put("Monday",m);
        map.put("Tuesday",t);
        map.put("Wednesday",w);
        map.put("Thursday",th);
        map.put("Friday",f);
        map.put("Saturday",s);
        map.put("Sunday",su);
        return map;
    }*/
    /*@ModelAttribute("weekdays")
    private Map<String,String> weekdaymap(){
        Map<String,String> map = new LinkedHashMap<>();
        map.put("Monday","월요일");
        map.put("Tuesday","화요일");
        map.put("Wednesday","수요일");
        map.put("Thursday","목요일");
        map.put("Friday","금요일");
        map.put("Saturday","토요일");
        map.put("Sunday","일요일");

        return map;
    }*/
}
