//package study.querydsl;
//
//import com.querydsl.core.BooleanBuilder;
//import com.querydsl.core.Tuple;
//import com.querydsl.core.types.Projections;
//import com.querydsl.core.types.dsl.BooleanExpression;
//import com.querydsl.core.types.dsl.CaseBuilder;
//import com.querydsl.core.types.dsl.Expressions;
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.EntityManagerFactory;
//import jakarta.persistence.PersistenceUnit;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Profile;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.test.annotation.Commit;
//import org.springframework.transaction.annotation.Transactional;
//import study.querydsl.dto.MemberDto;
//import study.querydsl.dto.MemberSearchCondition;
//import study.querydsl.dto.MemberTeamDto;
//import study.querydsl.dto.QMemberDto;
//import study.querydsl.entity.Member;
//import study.querydsl.entity.QMember;
//import study.querydsl.entity.Team;
//import study.querydsl.repository.MemberRepository;
//
//import java.util.List;
//
//import static com.querydsl.jpa.JPAExpressions.*;
//import static org.assertj.core.api.Assertions.assertThat;
//import static study.querydsl.entity.QMember.*;
//import static study.querydsl.entity.QTeam.team;
//
//@EnableJpaRepositories
//@SpringBootTest
//@Transactional
//@Profile("test")
//public class QuerydslBasicTest {
//
//    @PersistenceUnit
//    EntityManagerFactory emf;
//
//    @Autowired
//    EntityManager em;
//
//    JPAQueryFactory queryFactory;                   //동시성(멀티쓰레드) 문제 고민 안해도됨
//    @Autowired
//    private MemberRepository memberRepository;
//    //Spring에서 주입해주는 em자체가 멀티쓰레드에서 문제가 없도록 설계가 되어있음
//    //여러 멀티쓰레드에서 들어와도, 현재 트랜잭션이 어디에 걸려지에 따라서 그 트랜잭션에 바인딩 되도록 처리해줌
//
//    @BeforeEach
//    public void before() {
//        queryFactory = new JPAQueryFactory(em);
//
//        Team teamA = new Team("teamA");
//        Team teamB = new Team("teamB");
//        em.persist(teamA);
//        em.persist(teamB);
//
//        Member member1 = new Member("member1", 10, teamA);
//        Member member2 = new Member("member2", 20, teamA);
//
//        Member member3 = new Member("member3", 30, teamB);
//        Member member4 = new Member("member4", 40, teamB);
//        em.persist(member1);
//        em.persist(member2);
//        em.persist(member3);
//        em.persist(member4);
//    }
//
//    @Test
//    public void startJPQL() {
//        //finding member1 using JPQL
//        String qlString = "select m from Member m " +         //여기서 나는 문자 오류는 실행을하고, 메소드를 호출을 했을때,
//                "where m.username = :username";     //런타임 오류가 발생함.
//
//        Member findMember = em.createQuery(qlString, Member.class)
//                .setParameter("username", "member1")
//                .getSingleResult();
//
//        assertThat(findMember.getUsername()).isEqualTo("member1");
//    }
//
//    @Test
//    public void startQuerydsl() {
//        //finding member1 using querydsl
//        Member findMember = queryFactory
//                .select(member)
//                .from(member)
//                .where(member.username.eq("member1"))         //Parameter binding
//                .fetchOne();
//        //parameter binding 안해줘도 자동으로 해줌.
//        //문자를 사용하면 sql injection 공격에 취약한데 이 방법은 그걸 방지함.
//        //QType을 만들어서 컴파일 시점에 오류를 잡아줌.
//        assertThat(findMember.getUsername()).isEqualTo("member1");
//    }
//
//    @Test
//    public void search() {
//        Member findMember = queryFactory
//                .selectFrom(member)
//                .where(member.username.eq("member1")
//                        .and(member.age.eq(10)))
//                .fetchOne();
//
//        assertThat(findMember.getUsername()).isEqualTo("member1");
//    }
//
//    @Test
//    public void searchAndParam() {
//        Member findMember = queryFactory
//                .selectFrom(member)
//                .where(
//                        member.username.eq("member1"),
//                        member.age.eq(10)
//                )
//                .fetchOne();
//        //AND인 경우, 이렇게 풀어 쓸 수 있다
//        //중간에 null이 들어가면, null을 무시해서 동적 쿼리 사용할때 용이
//        assertThat(findMember.getUsername()).isEqualTo("member1");
//    }
//
////    @Test
////    public void resultFetch() {
////        QueryResults<Member> results = queryFactory
////                .selectFrom(member)
////                .fetchResults();
////
////        results.getTotal();
////        List<Member> content = results.getResults();
////    }
//
//    //정렬
//    @Test
//    public void sort() {
//        em.persist(new Member(null, 50));
//        em.persist(new Member("member5", 50));
//        em.persist(new Member("member6", 50));
//
//        List<Member> result = queryFactory
//                .selectFrom(member)
//                .where(member.age.eq(50))
//                .orderBy(
//                        member.age.desc(),
//                        member.username.asc().nullsLast()
//                )
//                .fetch();
//
//        Member member5 = result.get(0);
//        Member member6 = result.get(1);
//        Member nullMember = result.get(2);
//        assertThat(member5.getUsername()).isEqualTo("member5");
//        assertThat(member6.getUsername()).isEqualTo("member6");
//        assertThat(nullMember.getUsername()).isNull();
//    }
//
//    @Test
//    public void paging() {
//        List<Member> result = queryFactory
//                .selectFrom(member)
//                .orderBy(member.username.desc())
//                .offset(1)
//                .limit(2)
//                .fetch();
//
//        assertThat(result.size()).isEqualTo(2);
//    }
//
//    @Test
//    public void aggregation() {
//        Tuple result = queryFactory
//                .select(
//                        member.count(),
//                        member.age.sum(),
//                        member.age.avg(),
//                        member.age.max(),
//                        member.age.min()
//                )
//                .from(member)
//                .fetchOne();
//
//        assertThat(result.get(member.count())).isEqualTo(4);
//        assertThat(result.get(member.age.sum())).isEqualTo(100);
//        assertThat(result.get(member.age.avg())).isEqualTo(25);
//        assertThat(result.get(member.age.max())).isEqualTo(40);
//        assertThat(result.get(member.age.min())).isEqualTo(10);
//    }
//
//    //Team name and avg age of team members
//    @Test
//    public void group() {
//        List<Tuple> result = queryFactory
//                .select(team.name, member.age.avg())
//                .from(member)
//                .join(member.team, team)
//                .groupBy(team.name)
//                .fetch();
//
//        Tuple teamA = result.get(0);
//        Tuple teamB = result.get(1);
//
//        assertThat(teamA.get(team.name)).isEqualTo("teamA");
//        assertThat(teamB.get(team.name)).isEqualTo("teamB");
//        assertThat(teamA.get(member.age.avg())).isEqualTo(15);
//        assertThat(teamB.get(member.age.avg())).isEqualTo(35);
//    }
//
//    @Test
//    public void join() {
//        List<Member> results = queryFactory
//                .selectFrom(member)
//                .join(member.team, team)
//                .where(team.name.eq("teamA"))
//                .fetch();
//
//        for (Member result : results) {
//            System.out.println("Member= " + result);
//        }
//
////        assertThat(result)
////                .extracting("username")
////                .containsExactly("member1", "member2");
//    }
//
//    //회원의 이름이 팀의 이름이 같은 회원 조회
//    @Test
//    public void theta_join() {
//        em.persist(new Member("teamA"));
//        em.persist(new Member("teamB"));
//        em.persist(new Member("teamC"));
//
//        List<Member> result = queryFactory
//                .select(member)
//                .from(member, team)
//                .where(member.username.eq(team.name))
//                .fetch();
//
//        //from절에 여러 엔티티를 선택해서 세타 조인
//        //team의 모든 정보, member의 모든 정보를 가져와서 조인을 해버림
//
//        assertThat(result)
//                .extracting("username")
//                .containsExactly("teamA", "teamB");
//    }
//
//    //회원과 팀을 조인하면서, 팀 이름이 teamA인 팀만 조인, 회원은 모두 조회
//    //jpql: select m, t from Member m left join m.team t on t.name = 'teamA'
//    @Test
//    public void join_on_filtering() {
//        List<Tuple> result = queryFactory
//                .select(member, team)
//                .from(member)
//                .join(member.team, team)
////                .leftJoin(member.team, team)
////                .where(team.name.eq("teamA"))
//                .on(team.name.eq("teamA"))
//                .fetch();
//
//        for (Tuple tuple : result) {
//            System.out.println("tuple= " + tuple);
//        }
//    }
//
//    /*
//     *연관 관계 없는 엔티티 외부 조인
//     *회원의 이름이 팀의 이름이 같은 회원 조회
//     */
//    @Test
//    public void join_on_no_relation() {
//        em.persist(new Member("teamA"));
//        em.persist(new Member("teamB"));
//        em.persist(new Member("teamC"));
//
//        List<Tuple> result = queryFactory
//                .select(member, team)
//                .from(member)
//                .leftJoin(team)
//                .on(member.username.eq(team.name))  //조인하는 대상을 줄이는 on절
//                .fetch();
//
//        //from절에 여러 엔티티를 선택해서 세타 조인
//        //team의 모든 정보, member의 모든 정보를 가져와서 조인을 해버림
//
//        for (Tuple tuple : result) {
//            System.out.println("tuple= " + tuple);
//        }
//    }
//
//    @Test
//    public void fetchJoinNo() {
//        em.flush();
//        em.clear();
//
//        Member findMember = queryFactory
//                .selectFrom(member)
//                .where(member.username.eq("member1"))
//                .fetchOne();
//
//        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());
//        assertThat(loaded).as("패치조인 미적용").isFalse();
//    }
//
//    @Test
//    public void fetchJoin() {
//        em.flush();
//        em.clear();
//
//        Member findMember = queryFactory
//                .selectFrom(member)
//                .join(member.team, team)
//                .fetchJoin()
//                .where(member.username.eq("member1"))
//                .fetchOne();             //멤버를 조회할때 연관된 team을 하나의 쿼리로 한번에 끌고옴
//
//        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());
//        assertThat(loaded).as("패치조인 적용").isTrue();
//    }
//
//    /*
//    나이가 가장 많은 회원 조회
//     */
//    @Test
//    public void subQuery() {
//
//        QMember memberSub = new QMember("memberSub");
//
//        List<Member> result = queryFactory
//                .selectFrom(member)
//                .where(member.age.eq(
//                        select(memberSub.age.max())
//                                .from(memberSub)
//                ))
//                .fetch();
//
//        assertThat(result)
//                .extracting("age")
//                .containsExactly(40);
//    }
//
//    /*
//나이가 평균 이상인 회원
// */
//    @Test
//    public void subQueryGoe() {
//
//        QMember memberSub = new QMember("memberSub");
//
//        List<Member> result = queryFactory
//                .selectFrom(member)
//                .where(member.age.goe(
//                        select(memberSub.age.avg())
//                                .from(memberSub)
//                ))
//                .fetch();
//
//        assertThat(result)
//                .extracting("age")
//                .containsExactly(30, 40);
//    }
//
//    /*
//나이가 평균 이상인 회원
//*/
//    @Test
//    public void subQueryIn() {
//
//        QMember memberSub = new QMember("memberSub");
//
//        List<Member> result = queryFactory
//                .selectFrom(member)
//                .where(member.age.in(
//                        select(memberSub.age)
//                                .from(memberSub)
//                                .where(memberSub.age.gt(10))
//                ))
//                .fetch();
//
//        assertThat(result)
//                .extracting("age")
//                .containsExactly(20, 30, 40);
//    }
//
//    @Test
//    public void selectSubQuery() {
//
//        QMember memberSub = new QMember("memberSub");
//
//        List<Tuple> result = queryFactory
//                .select(member.username,
//                        select(memberSub.age.avg())
//                                .from(memberSub))
//                .from(member)
//                .fetch();
//
//        for (Tuple tuple : result) {
//            System.out.println("tuple = " + tuple);
//        }
//    }
//
//    @Test
//    public void basicCase() {
//        List<String> result = queryFactory
//                .select(member.age
//                        .when(10).then("열살")
//                        .when(20).then("스무살")
//                        .otherwise("기타"))
//                .from(member)
//                .fetch();
//
//        System.out.println(result);
//        for (String s : result) {
//            System.out.println("s = " + s);
//        }
//    }
//
//    @Test
//    public void complexCase() {
//        List<String> result = queryFactory
//                .select(new CaseBuilder()
//                        .when(member.age.between(0, 20)).then("0 ~ 20")
//                        .when(member.age.between(21, 30)).then("21 ~ 30")
//                        .otherwise("misc."))
//                .from(member)
//                .fetch();
//
//        System.out.println(result);
//        for (String s : result) {
//            System.out.println("s = " + s);
//        }
//    }
//
//    @Test
//    public void constant() {
//        List<Tuple> result = queryFactory
//                .select(member.username, Expressions.constant("A"))
//                .from(member)
//                .fetch();
//
//        System.out.println(result);
//        for (Tuple tuple : result) {
//            System.out.println("tuple = " + tuple);
//        }
//    }
//
//    @Test
//    public void concat() {
//        //{username}_{age}
//        List<String> result = queryFactory
//                .select(member.username.concat("_").concat(member.age.stringValue()))
//                .from(member)
//                .where(member.username.eq("member1"))
//                .fetch();
//
//        System.out.println(result);
//        for (String s : result) {
//            System.out.println("String = " + s);
//        }
//    }
//
//    @Test
//    public void tupleProjection() {
//        List<Tuple> result = queryFactory
//                .select(member.username, member.age)
//                .from(member)
//                .fetch();
//
//        for (Tuple tuple : result) {
//            String username = tuple.get(member.username);
//            Integer age = tuple.get(member.age);
//            System.out.println("username = " + username);
//            System.out.println("age = " + age);
//        }
//    }
//
//    @Test
//    public void findDtoByJPQL() {
//        List<MemberDto> result = em.createQuery(
//                        "select new study.querydsl.dto.MemberDto(m.username, m.age) " +
//                                "from Member m", MemberDto.class)
//                .getResultList();
//
//        System.out.println(result);
//        for (MemberDto memberDto : result) {
//            System.out.println("memberDto =" + memberDto);
//        }
//    }
//
//    @Test
//    public void findDtoBySetter() {
//        List<MemberDto> result = queryFactory
//                .select(Projections.bean(MemberDto.class,
//                        member.username,
//                        member.age))
//                .from(member)
//                .fetch();
//
//        System.out.println(result);
//        for (MemberDto memberDto : result) {
//            System.out.println("memberDto =" + memberDto);
//        }
//    }
//
//    @Test
//    public void findDtoByField() {
//        List<MemberDto> result = queryFactory
//                .select(Projections.fields(MemberDto.class,
//                        member.username,
//                        member.age))
//                .from(member)
//                .fetch();
//
//        System.out.println(result);
//        for (MemberDto memberDto : result) {
//            System.out.println("memberDto =" + memberDto);
//        }
//    }
//
//    @Test
//    public void findDtoByConstructor() {
//        List<MemberDto> result = queryFactory
//                .select(Projections.constructor(MemberDto.class,
//                        member.username,
//                        member.age))
//                .from(member)
//                .fetch();
//
//        System.out.println(result);
//        for (MemberDto memberDto : result) {
//            System.out.println("memberDto =" + memberDto);
//        }
//
//    }
//
//    @Test
//    public void findDtoByQueryProjection() {
//        List<MemberDto> result = queryFactory
//                .select(new QMemberDto(member.username, member.age))
//                .from(member)
//                .fetch();
//
//        System.out.println(result);
//        for (MemberDto memberDto : result) {
//            System.out.println("memberDto =" + memberDto);
//        }
//    }
//
//    @Test
//    public void dynamicQuery_BooleanBuilder() {
//        String usernameParam = "member1";
//        Integer ageParam = 10;
//
//        List<Member> result = searchMember1(usernameParam, ageParam);
//        assertThat(result.size()).isEqualTo(1);
//    }
//
//    private List<Member> searchMember1(String usernameCond, Integer ageCond) {
//        BooleanBuilder builder = new BooleanBuilder();
//        if (usernameCond != null) {
//            builder.and(member.username.eq(usernameCond));
//        }
//
//        if (ageCond != null) {
//            builder.and(member.age.eq(ageCond));
//        }
//
//        return queryFactory
//                .selectFrom(member)
//                .where(builder)
//                .fetch();
//    }
//
//    @Test
//    public void dynamicQuery_WhereParam() {
//        String usernameParam = "member1";
//        Integer ageParam = 10;
//
//        List<Member> result = searchMember2(usernameParam, ageParam);
//        assertThat(result.size()).isEqualTo(1);
//    }
//
//    private List<Member> searchMember2(String usernameCond, Integer ageCond) {
//        return queryFactory
//                .selectFrom(member)
//                .where(allEq(usernameCond, ageCond))
//                .fetch();
//    }
//
//
//    private BooleanExpression allEq(String usernameCond, Integer ageCond) {
//        return usernameEq(usernameCond).and(ageEq(ageCond));
//    }
//
//    private BooleanExpression usernameEq(String usernameCond) {
//        return usernameCond != null ? member.username.eq(usernameCond) : null;  //where에 null이 들어가면 그냥 무시함
//    }
//
//    private BooleanExpression ageEq(Integer ageCond) {
//        return ageCond != null ? member.age.eq(ageCond) : null;
//    }
//
//    @Test
//    @Commit
//    public void bulkUpdate() {
//        long count = queryFactory
//                .update(member)
//                .set(member.username, "비회원")
//                .where(member.age.lt(28))
//                .execute();
//
//        System.out.println("CcCCCCCC  " + count);
//        em.flush();
//        em.clear();
//
//        List<Member> result = queryFactory
//                .selectFrom(member)
//                .fetch();
//
//        System.out.println(result);
//    }
//
//    @Test
//    public void bulkAdd() {
//        long count = queryFactory
//                .update(member)
//                .set(member.age, member.age.add(1))  //multiple.....
//                .execute();
//    }
//
//    @Test
//    public void bulkDelete() {
//        long count = queryFactory
//                .delete(member)
//                .where(member.age.gt(18))
//                .execute();
//    }
//
//    @Test
//    public void sqlFunction() {
//        List<String> result = queryFactory
//                .select(member.username)
//                .from(member)
//                .where(member.username.eq(member.username.lower()))
//                .fetch();
//
//        for (String s : result) {
//            System.out.println("s = " + s);
//        }
//    }
//}
