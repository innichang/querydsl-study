package study.querydsl.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberAgeCategoryDto {
    private String username;
    private Integer age;
    private String ageCategory;

    public MemberAgeCategoryDto(String username, Integer age, String ageCategory) {
        this.username = username;
        this.age = age;
        this.ageCategory = ageCategory;
    }
}
