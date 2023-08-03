package study.querydsl.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import study.querydsl.dto.*;
import study.querydsl.entity.*;

import java.util.ArrayList;
import java.util.List;

import static com.querydsl.jpa.JPAExpressions.select;
import static org.springframework.util.StringUtils.hasText;
import static study.querydsl.entity.QMember.*;
import static study.querydsl.entity.QMemberProject.memberProject;
import static study.querydsl.entity.QTeam.*;

public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public MemberRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override         //and 사용
    public List<Member> queryWithAnd() {
        return queryFactory
                .selectFrom(member)
                .where(
                        member.username.contains("member")
                                .and(member.age.between(14, 21))
                )
                .fetch();
    }

//    @Override         //and 미사용사용
//    public List<Member> queryWithAnd() {
//        return queryFactory
//                .selectFrom(member)
//                .where(
//                        member.username.contains("member"),
//                        member.age.between(14, 21)
//                )
//                .fetch();
//    }

    @Override
    public List<Member> sortMemberList() {
        return queryFactory
                .selectFrom(member)
                .where(member.age.between(17, 25))
                .orderBy(
//                        member.age.asc()
                        member.username.asc().nullsLast(),
                        member.age.asc()
                )
                .fetch();
    }

    @Override
    public List<Member> pagingMemberList() {
        return queryFactory
                .selectFrom(member)
                .where(member.username.contains("member"))
                .orderBy(member.username.desc())
                .offset(2)
                .limit(4)
                .fetch();
    }

    @Override
    public AggregationDto aggregationList() {
        return queryFactory
                .select(new QAggregationDto(
                                member.count(),
                                member.age.sum(),
                                member.age.avg(),
                                member.age.max(),
                                member.age.min()
                        )
                )
                .from(member)
                .fetchOne();
    }

    @Override
    public List<Member> basicJoin() {
        return queryFactory
                .selectFrom(member)
                .join(member.team, team)
                .where(team.name.eq("teamA"))
                .fetch();
    }

    @Override
    public List<MemberTeamDto> joinOn() {
        List<Tuple> results = queryFactory
                .select(member, team)
                .from(member)
//                .join(member.team, team)
                .leftJoin(member.team, team)
//                .where(team.name.eq("teamA"))
                .on(team.name.eq("teamA"))
                .fetch();

        for (Tuple result : results) {
            System.out.println("result is " + result);
        }

        List<MemberTeamDto> memberTeamDto = new ArrayList<>();

        for (Tuple result : results) {
            Member member = result.get(0, Member.class);

            MemberTeamDto dto = new MemberTeamDto(
                    member.getId(),
                    member.getUsername(),
                    member.getAge(),
                    member.getTeam().getId(),
                    member.getTeam().getName()
            );

            memberTeamDto.add(dto);
        }

        return memberTeamDto;
    }

    @Override
    public List<MemberTeamDto> unrelatedJoin() {
        List<Tuple> results = queryFactory
                .select(member, team)
                .from(member)
                .leftJoin(team)
                .on(member.username.eq(team.name))
                .fetch();

        List<MemberTeamDto> memberTeamDto = new ArrayList<>();

        for (Tuple result : results) {
            Member member = result.get(0, Member.class);
            Team team = result.get(1, Team.class);

            if (team == null) continue;

            MemberTeamDto dto = new MemberTeamDto(
                    member.getId(),
                    member.getUsername(),
                    member.getAge(),
                    team.getId(),
                    team.getName()
            );

            memberTeamDto.add(dto);
        }

        return memberTeamDto;
    }

    @Override
    public Member fetchJoin() {
        return queryFactory
                .selectFrom(member)
                .join(member.team, team)
                .fetchJoin()
                .where(member.username.eq("member1"))
                .fetchOne();
    }

    @Override
    public List<Member> whereSubQuery() {
        QMember memberSub = new QMember("memberSub");

        return queryFactory
                .selectFrom(member)
                .where(member.age.in(
                        select(memberSub.age)
                                .from(memberSub)
                                .where(memberSub.age.gt(95))
                ))
                .fetch();
    }

    @Override
    public List<MemberAvgDto> selectSubQuery() {
        QMember memberSub = new QMember("memberSub");

        return queryFactory
                .select(Projections.constructor(MemberAvgDto.class,
                        member.username,
                        select(memberSub.age.avg())
                                .from(memberSub)
                ))
                .from(member)
                .fetch();
    }

    @Override
    public List<MemberAgeCategoryDto> caseExample() {
        return queryFactory
                .select(Projections.constructor(MemberAgeCategoryDto.class,
                                member.username,
                                member.age,
                                new CaseBuilder()
                                        .when(member.age.between(0, 9)).then("Too young to count")
                                        .when(member.age.between(10, 19)).then("Twenties")
                                        .when(member.age.between(20, 29)).then("Thirties")
                                        .when(member.age.between(30, 39)).then("Forties")
                                        .when(member.age.between(40, 49)).then("Fifties")
                                        .otherwise("Too old to count")))
                        .from(member)
                        .fetch();
    }

    @Override
    public List<String> addString() {
        return queryFactory
                .select(member.username.concat("_")
                        .concat(member.age.stringValue())
                        .concat("__")
                        .concat(member.team.name))
                .from(member)
                .where(member.age.between(61, 66))
                .fetch();
    }

    @Override
    public List<MemberTeamDto> queryProjections() {
        return queryFactory
                .select(new QMemberTeamDto(
                        member.id,
                        member.username,
                        member.age,
                        member.team.id,
                        member.team.name
                ))
                .from(member)
                .join(member.team, team)
                .where(member.team.name.eq("teamB"))
                .fetch();

    }

    @Override
    public List<MemberTeamDto> search(MemberSearchCondition condition) {
        return queryFactory
                .select(new QMemberTeamDto(
                        member.id.as("memberId"),
                        member.username,
                        member.age,
                        team.id.as("teamId"),
                        team.name.as("teamName")))
                .from(member)
                .leftJoin(member.team, team)
                .where(
                        usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe())
                )
                .fetch();
    }


    @Override
    public Page<MemberTeamDto> searchPageComplex(MemberSearchCondition condition, Pageable pageable) {

        List<MemberTeamDto> content = queryFactory
                .select(new QMemberTeamDto(
                        member.id.as("memberId"),
                        member.username,
                        member.age,
                        team.id.as("teamId"),
                        team.name.as("teamName")))
                .from(member)
                .leftJoin(member.team, team)
                .where(
                        usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(Wildcard.count)
                .from(member)
                .leftJoin(member.team, team)
                .where(
                        usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe())
                );

        return PageableExecutionUtils.getPage(content, pageable, () -> {
            return countQuery.fetch().get(0);
        });
        //content와 pageable를 보고, 필요없으면 countQuery.fetchCount()를 하지 않음
    }

    private BooleanExpression memberSearchCondition(String username, String teamName, Integer ageGoe, Integer ageLoe) {
        return usernameEq(username)
                .and(teamNameEq(teamName))
                .and(ageGoe(ageGoe))
                .and(ageLoe(ageLoe));
    }

    private BooleanExpression usernameEq(String username) {
        return hasText(username) ? member.username.contains(username) : null;
    }

    private BooleanExpression teamNameEq(String teamName) {
        return hasText(teamName) ? team.name.contains(teamName) : null;
    }

    private BooleanExpression ageGoe(Integer ageGoe) {
        return ageGoe != null ? member.age.goe(ageGoe) : null;
    }

    private BooleanExpression ageLoe(Integer ageLoe) {
        return ageLoe != null ? member.age.loe(ageLoe) : null;
    }

}




