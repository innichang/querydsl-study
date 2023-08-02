package study.querydsl.repository;

import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import study.querydsl.dto.ProjectMemberDto;
import study.querydsl.dto.QProjectMemberDto;
import study.querydsl.entity.QMember;
import study.querydsl.entity.QMemberProject;
import study.querydsl.entity.QProject;

import java.util.List;

import static study.querydsl.entity.QMember.member;
import static study.querydsl.entity.QMemberProject.memberProject;
import static study.querydsl.entity.QProject.*;
import static study.querydsl.entity.QProject.project;
import static study.querydsl.entity.QTeam.team;

public class MemberProjectRepositoryImpl implements MemberProjectRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public MemberProjectRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }
    public Page<ProjectMemberDto> pagingMtoM(Pageable pageable){
        List<ProjectMemberDto> content = queryFactory
                .select(new QProjectMemberDto(
                        memberProject.id,
                        memberProject.project.projectName,
                        memberProject.member.username,
                        memberProject.member.age))
                .from(memberProject)
                .leftJoin(memberProject.member, member)
                    .where(memberProject.member.id.eq(member.id))
                .leftJoin(memberProject.project, project)
                    .where(memberProject.project.id.eq(project.id))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .where(member.age.between(20,60)) //condition 추가 가능
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(Wildcard.count)
                .from(memberProject);

        return PageableExecutionUtils.getPage(content, pageable, () -> {
            return countQuery.fetch().get(0);
        });
    }

}
