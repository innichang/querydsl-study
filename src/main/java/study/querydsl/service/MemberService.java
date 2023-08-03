package study.querydsl.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import study.querydsl.dto.*;

import java.util.List;

public interface MemberService {
    List<MemberDto> queryWithAnd();

    List<MemberDto> sortMemberList();

    List<MemberDto> pagingMemberList();

    AggregationDto aggregationList();

    List<MemberTeamDto> basicJoin();

    List<MemberTeamDto> joinOn();

    List<MemberTeamDto> unrelatedJoin();

    MemberTeamDto fetchJoin();

    List<MemberDto> whereSubQuery();

    List<MemberAvgDto> selectSubQuery();

    List<MemberAgeCategoryDto> caseExmaple();

    List<String> addString();

    List<MemberTeamDto> queryProjections();

    List<MemberTeamDto> search(MemberSearchCondition memberSearchCondition);

    Page<MemberTeamDto> searchPageComplex(MemberSearchCondition condition, Pageable pageable);


}
