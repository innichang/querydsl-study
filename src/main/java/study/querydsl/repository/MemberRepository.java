package study.querydsl.repository;

import com.querydsl.core.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import study.querydsl.dto.MemberDto;
import study.querydsl.dto.MemberSearchCondition;
import study.querydsl.dto.MemberTeamDto;
import study.querydsl.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    List<Member> queryWithAnd();

    List<Member> sortMemberList();
    List<Member> pagingMemberList();
    Tuple aggregationList();
    List<Member> basicJoin ();
    List<MemberTeamDto> joinOn();
    List<MemberTeamDto> unrelatedJoin();
    Member fetchJoin();
    List<Member> whereSubQuery();
    List<Tuple> selectSubQuery();
    List<MemberDto> caseExample();
    List<String> addString();
    List<MemberTeamDto> queryProjections();
    List<MemberTeamDto> search(MemberSearchCondition memberSearchCondition);
    Page<MemberTeamDto> searchPageComplex(MemberSearchCondition condition, Pageable pageable);
















//    List<Member> findByUsername(String username);
//
//    @Modifying
//    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
//    int bulkAgePlus(@Param("age") int age);

}
