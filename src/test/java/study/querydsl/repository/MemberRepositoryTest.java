package study.querydsl.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.dto.MemberSearchCondition;
import study.querydsl.dto.MemberTeamDto;
import study.querydsl.entity.Member;
import study.querydsl.entity.QMember;
import study.querydsl.entity.Team;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static study.querydsl.entity.QMember.*;
import static study.querydsl.entity.QMember.member;
import static study.querydsl.entity.QTeam.team;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    EntityManagerFactory emf;

    @Autowired
    EntityManager em;

    JPAQueryFactory queryFactory;

    @Autowired
    MemberRepository memberRepository;


    @Test
    public void join() {     //N+1 조금 더 정확히 알아보기
        queryFactory = new JPAQueryFactory(em);
        Member findMember = queryFactory
                .selectFrom(member)
                .join(member.team, team)
                .on(member.username.eq("member1"))
                .fetchOne();

        System.out.println("member = " + findMember);     //멤버 조회 (1차 쿼리)
        System.out.println("member + team = " + findMember.getUsername() + " " + findMember.getTeam());
        //findMember.getTeam() 호출시 +1 쿼리 발생  (2차 쿼리)
    }

    @Test
    public void fetchJoinDemonstration() {
        queryFactory = new JPAQueryFactory(em);

        Member findMember = memberRepository.fetchJoin();

        System.out.println("findMember " + findMember);

        assertThat(findMember.getAge()).isEqualTo(1);
    }
}