package study.querydsl.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberProject {
    @Id
    @GeneratedValue
    @Column(name="member_project_mapping_id")
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "project_id")
    private Project project;
}
