package study.querydsl.repository;

import com.querydsl.core.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import study.querydsl.dto.*;
import study.querydsl.entity.Member;

import java.util.List;

public interface
MemberRepositoryCustom {

    List<Member> queryWithAnd();

    List<Member> sortMemberList();

    List<Member> pagingMemberList();

    AggregationDto aggregationList();

    List<Member> basicJoin();

    List<MemberTeamDto> joinOn();

    List<MemberTeamDto> unrelatedJoin();

    Member fetchJoin();

    List<Member> whereSubQuery();

    List<MemberAvgDto> selectSubQuery();

    List<MemberDto> caseExample();

    List<String> addString();

    List<MemberTeamDto> queryProjections();

    List<MemberTeamDto> search(MemberSearchCondition memberSearchCondition);

    Page<MemberTeamDto> searchPageComplex(MemberSearchCondition condition, Pageable pageable);


//
//    List<MemberTeamDto> search(MemberSearchCondition condition);
//
//
//    Page<MemberTeamDto> searchPageSimple(MemberSearchCondition condition, org.springframework.data.domain.Pageable pageable);
//
//    Page<MemberTeamDto> searchPageComplex(MemberSearchCondition condition, org.springframework.data.domain.Pageable pageable);
}
