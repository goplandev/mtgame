package kr.co.goplan.mtgame.domain.hardware;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "hardware_map")
@Getter
@Setter
public class HardwareMap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hardware_map_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hardware_id")
    private Hardware hardware; //장비

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hardware_group_id")
    private HardwareGroup hardwareGroup; //장비그룹

    private int isDelete;

    public static HardwareMap createHardwareMap(Hardware hardware) {
        HardwareMap hardwareMap = new HardwareMap();
        hardwareMap.setHardware(hardware);

        return hardwareMap;
    }
}
