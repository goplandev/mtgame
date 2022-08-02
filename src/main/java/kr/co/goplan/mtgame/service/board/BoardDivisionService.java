package kr.co.goplan.mtgame.service.board;

import kr.co.goplan.mtgame.domain.board.BoardDivision;
import kr.co.goplan.mtgame.domain.board.BoardDto;
import kr.co.goplan.mtgame.repository.board.BoardDivisionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BoardDivisionService {

    private final BoardDivisionRepository boardDivisionRepository;
    public BoardDivision findOne(Long id){
        //return memberRepository.findOne(id);

        //JpaRepository 사용시
        BoardDivision boardDivision = boardDivisionRepository.findById(id).get();
        return boardDivision;
    }

}
