package study.querydsl.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;
import study.querydsl.entity.Member;

@Data
@NoArgsConstructor
public class MemberDto {
    private String username;
    private Integer age;
    private String ageRange;

    @QueryProjection
    public MemberDto(Member member) {
        this.username = member.getUsername() != null ? member.getUsername() : null;
        this.age = member.getAge() != null ? member.getAge() : null;
    }

    public MemberDto(String username, Integer age, String ageRange){
        this.username = username;
        this.age = age;
        this.ageRange = ageRange;
    }
}
