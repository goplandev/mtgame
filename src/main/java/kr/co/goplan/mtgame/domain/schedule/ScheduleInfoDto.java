package kr.co.goplan.mtgame.domain.schedule;

import kr.co.goplan.mtgame.domain.app.AppInfo;
import kr.co.goplan.mtgame.domain.contents.Contents;
import kr.co.goplan.mtgame.domain.member.Member;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Data
@NoArgsConstructor
public class ScheduleInfoDto {
    private Long id;
    private AppInfo appInfo;
    //private Member register;
    //private Member modifier;

    private String title;
    private String description;

    private String createdBy;
    private String lastModifiedBy;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    /*@DateTimeFormat(pattern = "HH:mm")
    private LocalTime startTime;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime endTime;*/

    private Date startDate;
    private Date endDate;

    private Boolean useMonday = false;
    private Boolean useTuesday = false;
    private Boolean useWednesday = false;
    private Boolean useThursday = false;
    private Boolean useFriday = false;
    private Boolean useSaturday = false;
    private Boolean useSunday = false;

    private Boolean isUsed = true;

    private Boolean isDeleted = false;
    private List<ScheduleContentMappingDto> scheduleContentMappingDtos;
    private String scids;

    public ScheduleInfo toEntity(){
        return ScheduleInfo.builder()
                .id(id)
                .appInfo(appInfo)
                .title(title)
                .description(description)
                .startDate(startDate)
                .endDate(endDate)
                .useMonday(useMonday)
                .useTuesday(useTuesday)
                .useWednesday(useWednesday)
                .useThursday(useThursday)
                .useFriday(useFriday)
                .useSaturday(useSaturday)
                .useSunday(useSunday)
                .isDeleted(isDeleted)
                .build();

    }

    @Builder
    public ScheduleInfoDto(ScheduleInfo scheduleInfo) {
        this.id = scheduleInfo.getId();
        this.appInfo = scheduleInfo.getAppInfo();
        this.title = scheduleInfo.getTitle();
        this.description = scheduleInfo.getDescription();
        this.createdBy = scheduleInfo.getCreatedBy();
        this.lastModifiedBy = scheduleInfo.getLastModifiedBy();
        this.createdDate = scheduleInfo.getCreatedDate();
        this.lastModifiedDate = scheduleInfo.getLastModifiedDate();
        //this.startTime = scheduleInfo.getStartTime();
        //this.endTime = scheduleInfo.getEndTime();
        this.startDate = scheduleInfo.getStartDate();
        this.endDate = scheduleInfo.getEndDate();
        this.useMonday = scheduleInfo.getUseMonday();
        this.useTuesday = scheduleInfo.getUseTuesday();
        this.useWednesday = scheduleInfo.getUseWednesday();
        this.useThursday = scheduleInfo.getUseThursday();
        this.useFriday = scheduleInfo.getUseFriday();
        this.useSaturday = scheduleInfo.getUseSaturday();
        this.useSunday = scheduleInfo.getUseSunday();
        this.isDeleted = scheduleInfo.getIsDeleted();

        List<ScheduleContentMappingDto> list = new ArrayList<>();
        for (ScheduleContentMapping scheduleContentMapping : scheduleInfo.getScheduleContentMappings()) {
            ScheduleContentMappingDto scheduleContentMappingDto = new ScheduleContentMappingDto(scheduleContentMapping);
            list.add(scheduleContentMappingDto);
        }
        this.scheduleContentMappingDtos = list;


    }

}
