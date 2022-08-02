package kr.co.goplan.mtgame.service.schedule;

import kr.co.goplan.mtgame.domain.board.BoardDto;
import kr.co.goplan.mtgame.domain.contents.Contents;
import kr.co.goplan.mtgame.domain.schedule.ScheduleContentMapping;
import kr.co.goplan.mtgame.domain.schedule.ScheduleInfo;
import kr.co.goplan.mtgame.domain.schedule.ScheduleInfoDto;
import kr.co.goplan.mtgame.repository.contents.ContentsRepository;
import kr.co.goplan.mtgame.repository.schedule.ScheduleMappingRepository;
import kr.co.goplan.mtgame.repository.schedule.ScheduleRepository;
import kr.co.goplan.mtgame.util.FileUtilities;
import kr.co.goplan.mtgame.util.paging.Paged;
import kr.co.goplan.mtgame.util.paging.Paging;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Getter
@Setter
@Transactional(readOnly = true)
public class ScheduleService {
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ScheduleRepository scheduleRepository;
    private final ScheduleMappingRepository scheduleMappingRepository;
    private final ContentsRepository contentsRepository;

    @Transactional //변경
    public Long save(ScheduleInfoDto scheduleInfoDto) {

        ScheduleInfo scheduleInfo = scheduleRepository.save(scheduleInfoDto.toEntity());

        return scheduleInfo.getId();
    }
    
    //맵핑데이터 제거
    @Transactional 
    public Long deleteContentsMapping(Long smid) {

        ScheduleContentMapping scheduleContentMapping = scheduleMappingRepository.findById(smid).get();
        scheduleContentMapping.setIsDeleted(true);
        scheduleMappingRepository.save(scheduleContentMapping);

        return 1l;
    }


    @Transactional
    public Long editScheduleMap(ScheduleInfo scheduleInfo , String[] scids) {

        for (String s : scids) {
            Long hid = Long.parseLong(s);
            //엔티티조회
            Contents contents = contentsRepository.findById(hid).get();
            //맵핑 정보 생성
            ScheduleContentMapping scheduleContentMapping = ScheduleContentMapping.createScheduleContentMapping(contents);
            scheduleContentMapping.setScheduleInfo(scheduleInfo);
            scheduleInfo.addContent(scheduleContentMapping);
        }
        scheduleRepository.save(scheduleInfo);

        return scheduleInfo.getId();
    }
    @Transactional
    public Long editScheduleMap(ScheduleInfo scheduleInfo){
        scheduleRepository.save(scheduleInfo);
        return scheduleInfo.getId();
    }

    public Paged<ScheduleInfoDto> scheduleSearchPage(int pageNumber, int size, String keyword) {
        PageRequest request = PageRequest.of(pageNumber -1 , size , Sort.Direction.DESC,"id");
        Page<ScheduleInfoDto> scheduleInfoDtoList;
        if(keyword.equals("")){
            scheduleInfoDtoList = scheduleRepository.findAllDel(request);
        }else{
            scheduleInfoDtoList = scheduleRepository.findByTitleSearch(keyword,request);
        }
        return new Paged<>(scheduleInfoDtoList, Paging.of(scheduleInfoDtoList.getTotalPages(), pageNumber, size));
    }

    public ScheduleInfoDto findOne(Long id) {
        ScheduleInfoDto scheduleInfoDto = new ScheduleInfoDto(scheduleRepository.findById(id).get());
        return scheduleInfoDto;
    }

    public List<ScheduleInfo> findScheduleMapId(Long id){
        List<ScheduleInfo> scheduleInfo = scheduleRepository.findScheduleMapId(id);
        return scheduleInfo;
    }
}
