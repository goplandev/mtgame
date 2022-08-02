package kr.co.goplan.mtgame.domain.schedule;

import kr.co.goplan.mtgame.domain.BaseEntity;
import kr.co.goplan.mtgame.domain.app.AppInfo;
import kr.co.goplan.mtgame.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Entity
@Getter
@Setter
@Table(name = "schedule_info")
public class ScheduleInfo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_info_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_info_id")
    private AppInfo appInfo;//스케쥴대상

    private String title;

    private String description;


    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
    private Date startDate;
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
    private Date endDate;


    /*@DateTimeFormat(pattern = "HH:mm")
    private LocalTime startTime;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime endTime;*/

    private Boolean useMonday;
    private Boolean useTuesday;
    private Boolean useWednesday;
    private Boolean useThursday;
    private Boolean useFriday;
    private Boolean useSaturday;
    private Boolean useSunday;


    private Boolean isUsed;

    //@Column(columnDefinition = "bit default 0")
    private Boolean isDeleted = false;

    @OneToMany(mappedBy = "scheduleInfo" , cascade = CascadeType.ALL)
    private List<ScheduleContentMapping> scheduleContentMappings = new ArrayList<>();

    public void addContent(ScheduleContentMapping scheduleContentMapping){
        scheduleContentMappings.add(scheduleContentMapping);
    }

    @Builder
    public ScheduleInfo(Long id, AppInfo appInfo, String title, String description, Date endDate, Date startDate, Boolean useMonday, Boolean useTuesday, Boolean useWednesday, Boolean useThursday, Boolean useFriday, Boolean useSaturday, Boolean useSunday, Boolean isUsed, Boolean isDeleted) {
        this.id = id;
        this.appInfo = appInfo;
        this.title = title;
        this.description = description;
        this.endDate = endDate;
        this.startDate = startDate;
        //this.startTime = startTime;
        //this.endTime = endTime;
        this.useMonday = useMonday;
        this.useTuesday = useTuesday;
        this.useWednesday = useWednesday;
        this.useThursday = useThursday;
        this.useFriday = useFriday;
        this.useSaturday = useSaturday;
        this.useSunday = useSunday;
        this.isUsed = isUsed;
        this.isDeleted = isDeleted;
    }

    public ScheduleInfo(){

    }
    

}
