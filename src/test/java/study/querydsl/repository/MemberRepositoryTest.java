package study.querydsl.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.dto.AggregationDto;
import study.querydsl.entity.Member;
import study.querydsl.entity.Team;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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


    @BeforeEach
    public void before() {
        queryFactory = new JPAQueryFactory(em);

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);


        for(int i =0 ; i < 10; i++){
            if(i%2 == 0) {
                em.persist(new Member("member" + i, i, teamA));
            } else {
                em.persist(new Member("member"+ i, i, teamB));
            }
        }
    }

    @Test     //14세와 21세 사이
    public void join_and_test() {
        List<Member> findMembers = memberRepository.queryWithAnd();

        assertThat(findMembers).hasSize(1);
        assertThat(findMembers)
                .extracting("username")
                .containsExactly("member2");
    }

    @Test
    public void aggregation_test() {
        AggregationDto result = memberRepository.aggregationList();

        assertThat(result.getAvg()).isEqualTo(4.5);
        assertThat(result.getSum()).isEqualTo(45);
    }

    @Test
    public void pagingTest() {
        List<Member> results = memberRepository.pagingMemberList();

        assertThat(results).hasSize(2);
        assertThat(results)
                .extracting("username")
                .containsExactly("member9", "member8");
    }

    @Test
    public void basicJoin_test() {
        List<Member> members = memberRepository.basicJoin();

        assertThat(members).hasSize(50);
        assertThat(members)
                .extracting("age")
                .doesNotContain(3,5,7,9);
    }

//    @Test
//    public void joinOn_test(){
//        List<MemberTeamDto> members = memberRepository.joinOn();
//
//        assertThat(members).hasSize(5);
//    }

    @Test   //Test XXXXXX
    public void join() {     //N+1 조금 더 정확히 알아보는 용도
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
//
//    @Test
//    public void fetchJoinDemonstration() {
//        queryFactory = new JPAQueryFactory(em);
//
//        Member findMember = memberRepository.fetchJoin();
//
//        System.out.println("findMember " + findMember);
//
//        assertThat(findMember.getAge()).isEqualTo(1);
//    }
}