package kr.co.goplan.mtgame.domain.hardware;

import kr.co.goplan.mtgame.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "hardware_group")
public class HardwareGroup extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hardware_group_id")
    private Long id;

    @OneToMany(mappedBy = "hardwareGroup", cascade = CascadeType.ALL)
    private List<HardwareMap> hardwareMaps = new ArrayList<>();

    public void addHardWare(HardwareMap hardwareMap) {
        hardwareMaps.add(hardwareMap);
        hardwareMap.setHardwareGroup(this);
    }
    //생성 메서드
    /*public static HardwareGroup createHardWareGroup(Member member,HardwareMap... hardwareMaps){

        HardwareGroup hardwareGroup = new HardwareGroup();
        hardwareGroup.setMember(member);
        for (HardwareMap hardwareMap : hardwareMaps){
            hardwareGroup.addHardWare(hardwareMap);
        }
        hardwareGroup.setRegdate(LocalDateTime.now());

        return hardwareGroup;
    }*/
    public static HardwareGroup createHardWareGroup(HardwareMap... hardwareMaps){


        HardwareGroup hardwareGroup = new HardwareGroup();
        for (HardwareMap hardwareMap : hardwareMaps){
            hardwareGroup.addHardWare(hardwareMap);
        }
        return hardwareGroup;
    }


    //삭제
    public void delete(){
        this.setIsDelete(1);
        for (HardwareMap hardwareMap : hardwareMaps){
            hardwareMap.setIsDelete(1);
        }
    }
    private String name;
    private String description;

    private String hwids;
    private int isDelete;
}
