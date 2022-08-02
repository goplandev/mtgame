package kr.co.goplan.mtgame.service.hardware;

import kr.co.goplan.mtgame.domain.hardware.Hardware;
import kr.co.goplan.mtgame.domain.hardware.HardwareGroup;
import kr.co.goplan.mtgame.domain.hardware.HardwareMap;
import kr.co.goplan.mtgame.repository.hardware.HardwareGroupRepository;
import kr.co.goplan.mtgame.repository.hardware.HardwareRepository;
import kr.co.goplan.mtgame.repository.member.MemberRepository;
import kr.co.goplan.mtgame.util.paging.Paged;
import kr.co.goplan.mtgame.util.paging.Paging;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HardWareGroupService {
    private final MemberRepository memberRepository;
    private final HardwareRepository hardwareRepository;
    private final HardwareGroupRepository hardwareGroupRepository;
    @Transactional
    public void saveHardWareGroup(HardwareGroup hardwareGroup){
        hardwareGroupRepository.save(hardwareGroup);
    }
    public void saveHardWareGroup(HardwareGroup hardwareGroup , Hardware hardware){
        hardwareGroupRepository.save(hardwareGroup);
    }
    @Transactional
    public Long saveHardwareMap(Long memberId , Long hardwareId){

        Hardware hardware = hardwareRepository.findById(hardwareId).get();

        //맵핑 정보 생성
        HardwareMap hardwareMap = HardwareMap.createHardwareMap(hardware);

        //그룹 생성
        HardwareGroup hardwareGroup = HardwareGroup.createHardWareGroup(hardwareMap);

        hardwareGroupRepository.save(hardwareGroup);

        return hardwareGroup.getId();

    }
    @Transactional
    public Long editHardwareMap(HardwareGroup hardwareGroup , Long hardwareId){
        //엔티티조회
        Hardware hardware = hardwareRepository.findById(hardwareId).get();

        //맵핑 정보 생성
        HardwareMap hardwareMap = HardwareMap.createHardwareMap(hardware);

        hardwareGroup.addHardWare(hardwareMap);

        hardwareGroupRepository.save(hardwareGroup);

        return hardwareGroup.getId();

    }
    @Transactional
    public Long editHardwareMap(HardwareGroup hardwareGroup , String[] hardwareIds){

        for (String s:hardwareIds) {
            Long hid = Long.parseLong(s);
            //엔티티조회
            Hardware hardware = hardwareRepository.findById(hid).get();
            //맵핑 정보 생성
            HardwareMap hardwareMap = HardwareMap.createHardwareMap(hardware);

            hardwareGroup.addHardWare(hardwareMap);
        }

        hardwareGroupRepository.save(hardwareGroup);

        return hardwareGroup.getId();

    }
    @Transactional
    public Long editHardwareMap(HardwareGroup hardwareGroup){

        hardwareGroupRepository.save(hardwareGroup);

        return hardwareGroup.getId();

    }

    public List<HardwareGroup> hardwareGroupList(){
        return hardwareGroupRepository.findAll();
    }

    public Paged<HardwareGroup> hardwareGroupListPage(int pageNumber, int size){
        PageRequest request = PageRequest.of(pageNumber -1 , size , Sort.Direction.DESC,"id");
        //Page<HardwareGroup> hardwareGroupList = hardwareGroupDAO.findAll(request);
        Page<HardwareGroup> hardwareGroupList = hardwareGroupRepository.findAllDel(request);
        return new Paged<>(hardwareGroupList, Paging.of(hardwareGroupList.getTotalPages(), pageNumber, size));
    }
    public Paged<HardwareGroup> hardwareGroupListPage(int pageNumber, int size , List<Long> hwgids){
        PageRequest request = PageRequest.of(pageNumber -1 , size , Sort.Direction.DESC,"id");
        Page<HardwareGroup> hardwareGroupList = hardwareGroupRepository.findByIdSearch(hwgids,request);
        return new Paged<>(hardwareGroupList, Paging.of(hardwareGroupList.getTotalPages(), pageNumber, size));
    }
    public HardwareGroup findOne(Long hardWareGroupid){

        return hardwareGroupRepository.findById(hardWareGroupid).get();
    }

    /*public List<HardwareGroup> searchhardwareGroup(MySearch search){
        return hardWareGroupRepository.findAll(search);
    }*/
    public List<HardwareGroup> findAllWithItem(){
        List<HardwareGroup> hardwareGroups = hardwareGroupRepository.findAllWithItem();
        //Dto 사용시
        //List<HardwareGroup> result = orders.stream().map(o -> new HardwareGroup(o)).collect(toList());
        //return result;
        return hardwareGroups;
    }
    public List<HardwareGroup> findAllHardware(Long id){
        List<HardwareGroup> hardwareGroups = hardwareGroupRepository.findAllHardware(id);
        //Dto 사용시
        //List<HardwareGroup> result = orders.stream().map(o -> new HardwareGroup(o)).collect(toList());
        //return result;
        return hardwareGroups;
    }

    /*@Transactional
    public Long deleteHardwareMapping(Long hardwareMapId){
        //엔티티조회
        HardwareMap hardwareMap = hardwareGroupRepository.findOneMap(hardwareMapId);
        hardwareMap.setIsDelete(1);
        hardwareRepository.save(hardwareMap);

        return hardwareMap.getId();
    }*/
}
