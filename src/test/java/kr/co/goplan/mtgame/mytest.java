package kr.co.goplan.mtgame;

import kr.co.goplan.mtgame.api.board.BoardApiController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.logging.Logger;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = MtgameApplication.class)

public class mytest {


    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(mytest.class);
    @Value("#{config['video.allow']}")
    private String videoAllow;

    @Test
    public void contextLoads() throws Exception{
        logger.info("videoAllow : " +videoAllow);
    }
}
