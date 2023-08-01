package study.querydsl.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProjectMemberDto {
    private Long id;
    private String projectName;
    private String memberName;
    private Integer age;

    public ProjectMemberDto(Long id, String projectName, String memberName, Integer age){
        this.id = id;
        this.projectName = projectName;
        this.memberName = memberName;
        this.age = age;
    }
}
