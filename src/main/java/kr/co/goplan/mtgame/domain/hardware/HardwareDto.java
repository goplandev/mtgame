package kr.co.goplan.mtgame.domain.hardware;

import lombok.Getter;

@Getter
public class HardwareDto {
    private Long id;
    private String name;
    private String description;
    //private List<ScheduleMapDto> scheduleMappings;

    public HardwareDto( Hardware hardware){
        id = hardware.getId();
        name = hardware.getName();
        description = hardware.getDescription();
        /*List<ScheduleMapDto> list = new ArrayList<>();
        for (ScheduleMapping sm : hardware.getScheduleMappings()) {
            ScheduleMapDto smd = new ScheduleMapDto(sm);
            list.add(smd);
        }

        scheduleMappings = list;*/
    }
}
