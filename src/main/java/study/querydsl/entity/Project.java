package study.querydsl.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of ={"id", "projectName"})
public class Project {
    @Id
    @GeneratedValue
    @Column(name = "project_id")
    private Long id;

    @Column(name="project_name")
    private String projectName;

    @JsonIgnore
    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private List<MemberProject> projects = new ArrayList<>();
}
