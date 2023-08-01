package study.querydsl.controller;

import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import study.querydsl.dto.*;
import study.querydsl.entity.MemberProject;
import study.querydsl.repository.MemberRepositoryImpl;
import study.querydsl.service.MemberService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final MemberRepositoryImpl memberRepository;

    @GetMapping("/members/search/and")
    public List<MemberDto> andMember() {
        return memberService.queryWithAnd();
    }

    @GetMapping("/members/sorting")
    public List<MemberDto> sortMemberList() {
        return memberService.sortMemberList();
    }

    @GetMapping("/members/paging")
    public List<MemberDto> pagingMemberList() {
        return memberService.pagingMemberList();
    }

    @GetMapping("/members/aggregate")
    public AggregationDto aggregationList() {
        return memberService.aggregationList();
    }

    @GetMapping("/members/basicjoin")
    public List<MemberTeamDto> basicJoin() {
        return memberService.basicJoin();
    }

    @GetMapping("/members/joinOn")
    public List<MemberTeamDto> joinOn() {
        return memberService.joinOn();
    }

    @GetMapping("/members/unrelatedJoin")
    public List<MemberTeamDto> unrelatedJoin() {
        return memberService.unrelatedJoin();
    }

    @GetMapping("/members/fetchJoin")
    public MemberTeamDto fetchJoin() {
        return memberService.fetchJoin();
    }

    @GetMapping("/members/whereSubQuery")
    List<MemberDto> whereSubQuery() {
        return memberService.whereSubQuery();
    }

    @GetMapping("/members/selectSubQuery")
    List<MemberAvgDto> selectSubQuery() {
        return memberService.selectSubQuery();
    }

    @GetMapping("/members/caseExample")
    List<MemberDto> caseExample() {
        return memberService.caseExmaple();
    }

    @GetMapping("/members/addString")
    List<String> addString() {
        return memberService.addString();
    }

    @GetMapping("/members/queryProjections")
    List<MemberTeamDto> queryProjections() {
        return memberService.queryProjections();
    }

    @GetMapping("/members/search")
    List<MemberTeamDto> search() {
        MemberSearchCondition memberSearchCondition = new MemberSearchCondition();
        memberSearchCondition.setUsername("member");
        memberSearchCondition.setAgeGoe(3);
        memberSearchCondition.setAgeLoe(20);
        memberSearchCondition.setTeamName("teamB");

        return memberService.search(memberSearchCondition);
    }

    @GetMapping("/members/search/page")
    Page<MemberTeamDto> searchPageComplex(Pageable pageable) {
        MemberSearchCondition condition = new MemberSearchCondition();
        condition.setUsername("member");
        condition.setAgeGoe(3);
        condition.setAgeLoe(99);
        condition.setTeamName("teamB");

        return memberService.searchPageComplex(condition, pageable);
    }

    @GetMapping("/members/mtom")
    List<MemberProject> mTom(){
        return memberRepository.mToMQueryDsl();
    }
}
