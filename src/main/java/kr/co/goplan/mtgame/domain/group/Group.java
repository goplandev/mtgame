package kr.co.goplan.mtgame.domain.group;

import kr.co.goplan.mtgame.domain.member.Member;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id","name"})
public class Group {
    @Id
    @GeneratedValue
    private Long id;
    private String name;

    @OneToMany(mappedBy = "group")
    private List<Member> members = new ArrayList<>();

    public Group(String name){
        this.name = name;
    }
}
