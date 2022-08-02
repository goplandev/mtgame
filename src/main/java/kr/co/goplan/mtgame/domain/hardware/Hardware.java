package kr.co.goplan.mtgame.domain.hardware;

import kr.co.goplan.mtgame.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter@Setter
@Table(name = "hardware")
public class Hardware extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hardware_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hardware_group_id")
    private HardwareGroup hardwareGroup;

    //삭제
    public void delete(){
        this.setIsDelete(1);
        /*for (SensorMapping sensorMapping : sensorMappings){
            sensorMapping.setIsDelete(1);
        }*/
    }
    private String scids;
    // name
    @Column(unique = true)
    private String name;

    // ip
    private String ip;

    // mac
    private String mac;
    // is_power
    private int isPower;

    // is_delete
    private int isDelete;

    // is_use
    private int isUse;

    private String description;
}
