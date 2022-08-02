package kr.co.goplan.mtgame.service.hardware;

import kr.co.goplan.mtgame.domain.hardware.Hardware;
import kr.co.goplan.mtgame.domain.hardware.HardwareDto;
import kr.co.goplan.mtgame.domain.member.Member;
import kr.co.goplan.mtgame.domain.member.MemberDto;
import kr.co.goplan.mtgame.repository.hardware.HardwareRepository;
import kr.co.goplan.mtgame.util.paging.Paged;
import kr.co.goplan.mtgame.util.paging.Paging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class HardWareService {
    @Autowired
    HardwareRepository hardwareRepository;
    public HardWareService(HardwareRepository hardwareRepository ) {
        this.hardwareRepository = hardwareRepository;
    }
    @Transactional
    public void saveHardWare(Hardware hardware){
        hardwareRepository.save(hardware);
    }

    public List<Hardware> hardwareList(){
        return hardwareRepository.findAll();
    }
    public Paged<Hardware> hardwareListPage(int pageNumber, int size) {
        PageRequest request = PageRequest.of(pageNumber -1 , size , Sort.Direction.DESC,"id");
        //Page<Hardware> hardwareList = hardwareDAO.findAll(request);
        Page<Hardware> hardwareList = hardwareRepository.findAllDel(request);
        return new Paged<>(hardwareList, Paging.of(hardwareList.getTotalPages(), pageNumber, size));
    }
    public Paged<Hardware> hardwareListPage(int pageNumber, int size , List<Long> hwids) {
        PageRequest request = PageRequest.of(pageNumber -1 , size , Sort.Direction.DESC,"id");
        Page<Hardware> hardwareList = hardwareRepository.findByIdsSearch(hwids,request);
        return new Paged<>(hardwareList, Paging.of(hardwareList.getTotalPages(), pageNumber, size));
    }
    public Hardware findOne(Long hardwareId){
        Hardware hardware = hardwareRepository.findById(hardwareId).get();
        return hardware;
    }

    public HardwareDto findOneName(String hardware_name) {
        Optional <Hardware> hardwareWrapper = hardwareRepository.findByName(hardware_name);
        HardwareDto hardwareDto = null;
        if(!hardwareWrapper.isEmpty()){
            hardwareDto = new HardwareDto(hardwareWrapper.get());
        }
        return hardwareDto;
    }

}
