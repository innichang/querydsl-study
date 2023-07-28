package study.querydsl.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;
import study.querydsl.entity.Member;

@Data
@NoArgsConstructor
public class AggregationDto {
    private Long count;
    private Integer sum;
    private Double avg;
    private Integer max;
    private Integer min;

    @QueryProjection
    public AggregationDto(Long count,
                          Integer sum,
                          Double avg,
                          Integer max,
                          Integer min) {
        this.count = count;
        this.sum = sum;
        this.avg = avg;
        this.max = max;
        this.min = min;
    }
}
