package kr.co.goplan.mtgame.domain.schedule;

import kr.co.goplan.mtgame.domain.contents.Contents;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "schedule_content_mapping")
public class ScheduleContentMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_content_mapping_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contents_id")
    private Contents contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_info_id")
    private ScheduleInfo scheduleInfo;

    private int exposureTime;

    private int dispNo;

    private Boolean isDeleted = false;

    public static ScheduleContentMapping createScheduleContentMapping(Contents contents) {
        ScheduleContentMapping scheduleContentMapping = new ScheduleContentMapping();
        scheduleContentMapping.setContents(contents);

        return scheduleContentMapping;
    }
}
