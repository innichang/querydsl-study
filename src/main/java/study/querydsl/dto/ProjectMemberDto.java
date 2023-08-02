package study.querydsl.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import study.querydsl.entity.MemberProject;

@Data
@NoArgsConstructor
public class ProjectMemberDto {
    private Long id;
    private String projectName;
    private String username;
    private Integer age;

    @QueryProjection
    public ProjectMemberDto(Long id, String projectName, String username, Integer age) {
        this.id = id;
        this.projectName = projectName;
        this.username = username;
        this.age = age;
    }
}
