package study.querydsl.dto;

import com.querydsl.core.Tuple;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import study.querydsl.entity.Member;

@Data
public class MemberTeamDto {
    private Long memberId;
    private String username;
    private Integer age;
    private Long teamId;
    private String teamName;

    @QueryProjection
    public MemberTeamDto(Member member) {
        this.memberId = member.getId();
        this.username = member.getUsername();
        this.age = member.getAge();
        this.teamId = member.getTeam().getId();
        this.teamName = member.getTeam().getName();
    }

    @QueryProjection
    public MemberTeamDto(Long memberId,
                         String username,
                         Integer age,
                         Long teamId,
                         String teamName) {
        this.memberId = memberId;
        this.username = username;
        this.age = age;
        this.teamId = teamId;
        this.teamName = teamName;
    }
}
