package study.querydsl.service;

import com.querydsl.core.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import study.querydsl.dto.MemberDto;
import study.querydsl.dto.MemberSearchCondition;
import study.querydsl.dto.MemberTeamDto;
import study.querydsl.entity.Member;

import java.util.List;

public interface MemberService {
    List<MemberDto> queryWithAnd();
    List<MemberDto> sortMemberList();
    List<MemberDto> pagingMemberList();
    Tuple aggregationList();
    List<MemberTeamDto> basicJoin();
    List<MemberTeamDto> joinOn();
    List<MemberTeamDto> unrelatedJoin();
    MemberTeamDto fetchJoin();
    List<MemberDto> whereSubQuery();
    List<Tuple> selectSubQuery();
    List<MemberDto> caseExmaple();
    List<String> addString();
    List<MemberTeamDto> queryProjections();
    List<MemberTeamDto> search(MemberSearchCondition memberSearchCondition);
    Page<MemberTeamDto> searchPageComplex(MemberSearchCondition condition, Pageable pageable);


}
