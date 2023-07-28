package study.querydsl.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;
import study.querydsl.entity.Member;

@Data
@NoArgsConstructor
public class MemberAvgDto {
    private String username;
    private Double avgAge;

    public MemberAvgDto(String username, Double avgAge) {
        this.username = username;
        this.avgAge = avgAge;
    }
}
