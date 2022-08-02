package kr.co.goplan.mtgame.init;

import kr.co.goplan.mtgame.domain.app.AppInfo;
import kr.co.goplan.mtgame.domain.board.BoardDivision;
import kr.co.goplan.mtgame.domain.member.Member;
import kr.co.goplan.mtgame.domain.member.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class InitDB {

    private final InitService initService;
    @PostConstruct
    public void init() throws InterruptedException {
        //initService.dbInit1();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;

        public void dbInit1() throws InterruptedException {

            Member member1 = createMember("flashjavac@naver.com", "flashjavac@naver.com", "01043251203", "flashjavac@naver.com", "1111", Role.ADMIN);
            em.persist(member1);
            Member member2 = createMember("flashgo@nate.com", "flashgo@nate.com", "01047268464", "flashgo@nate.com", "1111", Role.MEMBER);
            em.persist(member2);
            Member member3 = createMember("gardeniot@naver.com", "gardeniot@naver.com", "01012345678", "gardeniot@naver.com", "1111", Role.MEMBER);
            em.persist(member3);
            Member member4 = createMember("gardeniot1@naver.com", "gardeniot1@naver.com", "01012345678", "gardeniot@naver.com", "1111", Role.MEMBER);
            em.persist(member3);

            Thread.sleep(500);
            BoardDivision boardDivision1 = createBoardDivision("자유게시판");
            em.persist(boardDivision1);

            BoardDivision boardDivision2 = createBoardDivision("관리자게시판");
            em.persist(boardDivision2);
            AppInfo hw = createAppInfo("dev01", "테스트 APP 01");
            em.persist(hw);
            Thread.sleep(50);
            AppInfo hw2 = createAppInfo("dev02","테스트 APP 02");
            em.persist(hw2);
            Thread.sleep(50);

/*
            Hardware hw = createHW("dev01", "127.0.0.1", "1211342325", "테스트 장비");
            em.persist(hw);
            Thread.sleep(50);
            Hardware hw2 = createHW("dev02", "127.0.0.2", "1211342325","테스트 장비");
            em.persist(hw2);
            Thread.sleep(50);
            Hardware hw3 = createHW("dev03", "127.0.0.3", "1211342325","테스트 장비");
            em.persist(hw3);
            Thread.sleep(500);

            HardwareGroup hwg = createHWG("dev groupA","테스트 A그룹");
            em.persist(hwg);
            HardwareGroup hwg2 = createHWG("dev groupB","테스트 B그룹");
            em.persist(hwg2);
            HardwareGroup hwg3 = createHWG("dev groupC","테스트 B그룹");
            em.persist(hwg3);
            Thread.sleep(500);

            Schedule sc = createSC("schedule testA","테스트 A스케쥴");
            em.persist(sc);
            Schedule sc2 = createSC("schedule testB","테스트 B스케쥴");
            em.persist(sc2);
            Schedule sc3 = createSC("schedule testC","테스트 C스케쥴");
            em.persist(sc3);
            Thread.sleep(500);

            Sensor ss0 = createSS("조도", SensorType.lighting,1,"조명센서");
            Sensor ss1 = createSS("습도", SensorType.humidity,1,"습도센서");
            Sensor ss2 = createSS("바람", SensorType.wind,1,"바람센서");
            Sensor ss3 = createSS("온도", SensorType.temperature,1,"온도센서");
            Sensor ss4 = createSS("물", SensorType.water,1,"수량센서");
            em.persist(ss1);
            em.persist(ss2);
            em.persist(ss3);
            em.persist(ss4);
            em.persist(ss0);
            Thread.sleep(500);

            for(int i = 0 ; i <12;i++){
                createInfo(i);
            }*/
        }
        private BoardDivision createBoardDivision(String name) {
            BoardDivision b = new BoardDivision();
            b.setName(name);
            b.setRegisteredDatetime(LocalDateTime.now());
            return b;
        }

        private Member createMember(String name, String email, String phone,
                                    String sns, String pw, Role role) {
            Member member = new Member();
            member.setName(name);
            member.setEmail(email);
            member.setPhone(phone);
            //BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            //member.setPassword(passwordEncoder.encode(pw));
            member.setPassword(pw);
            member.setRole(role);
            //member.setRegisteredDatetime(LocalDateTime.now());
            return member;
        }
        private AppInfo createAppInfo(String name ,String ds) {
            AppInfo hw = new AppInfo();
            hw.setName(name);
            hw.setDescription(ds);
            //hw.setRegisteredDatetime(LocalDateTime.now());
            //hw.setModifiedDatetime(LocalDateTime.now());
            return hw;
        }
/*
        private Hardware createHW(String name, String ip, String mac ,String ds) {
            Hardware hw = new Hardware();
            hw.setName(name);
            hw.setIp(ip);
            hw.setMac(mac);
            hw.setDescription(ds);
            hw.setRegdate(LocalDateTime.now());
            hw.setEditdate(LocalDateTime.now());
            return hw;
        }
        private HardwareGroup createHWG(String name,String ds) {
            HardwareGroup hwg = new HardwareGroup();
            hwg.setName(name);
            hwg.setDescription(ds);
            hwg.setRegdate(LocalDateTime.now());
            hwg.setEditdate(LocalDateTime.now());
            return hwg;
        }
        private Schedule createSC(String name,String ds) {
            Schedule sc = new Schedule();
            sc.setName(name);
            sc.setScheduleType(ScheduleType.WATER_PUMP_C);
            sc.setEventType(EventType.ON);
            sc.setStartTime(LocalTime.now());
            *//*sc.setEndTime(LocalTime.now().plusHours(1));*//*
            sc.setDescription(ds);
            sc.setRegdate(LocalDateTime.now());
            return sc;
        }

        private Sensor createSS(String name,SensorType st , int pw, String ds ) {
            Sensor sc = new Sensor();
            sc.setName(name);
            sc.setSensorType(st);
            sc.setIsPower(pw);
            sc.setDescription(ds);
            return sc;
        }
        private void createInfo(int i ) {
            Info info = new Info();
            int re = i% 2;
            if(re == 0){
                Hardware ha = new Hardware();
                ha.setId(4l);
                info.setHardware(ha);
                info.setHardware_name("dev01");
            }else{
                Hardware ha2 = new Hardware();
                ha2.setId(5l);
                info.setHardware(ha2);
                info.setHardware_name("dev02");
            }
            double lan0 = (Math.random()*30)+10;
            info.setTem((long)lan0);
            double lan = (Math.random()*20)+5;
            info.setHum((long)lan);
            double lan1 = (Math.random()*2)+0;
            double lan2 = (Math.random()*2)+0;
            double lan3 = (Math.random()*10)+3;
            info.setLux((long)lan0-5);
            info.setPm2p5((long)lan3);
            info.setWlevel((long)lan2);
            info.setRd1((long)lan1);
            info.setRd2((long)lan2);
            info.setRd3((long)lan1);
            info.setRd4((long)lan2);
            info.setRd5((long)lan1);
            info.setRegdate(LocalDateTime.now().plusMinutes(i));
            em.persist(info);
        }*/
    }
}
