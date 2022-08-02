package kr.co.goplan.mtgame.domain.schedule;

import kr.co.goplan.mtgame.domain.contents.Contents;
import lombok.Getter;

@Getter
public class ScheduleContentMappingDto {
    private Long id;
    private Contents contents;
    private ScheduleInfo scheduleInfo;
    private int exposureTime;
    private int dispNo;

    public ScheduleContentMappingDto(ScheduleContentMapping scheduleContentMapping) {
        id = scheduleContentMapping.getId();
        contents = scheduleContentMapping.getContents();
        scheduleInfo = scheduleContentMapping.getScheduleInfo();
        exposureTime = scheduleContentMapping.getExposureTime();
        dispNo = scheduleContentMapping.getDispNo();
    }
}
