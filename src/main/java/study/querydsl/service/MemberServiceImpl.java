package study.querydsl.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import study.querydsl.dto.*;
import study.querydsl.entity.Member;
import study.querydsl.repository.MemberRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public List<MemberDto> queryWithAnd() {
        return memberRepository.queryWithAnd().stream()
                .map(MemberDto::new)
                .toList();
    }

    @Override
    public List<MemberDto> sortMemberList() {
        return memberRepository.sortMemberList().stream()
                .map(MemberDto::new)
                .toList();
    }

    @Override
    public List<MemberDto> pagingMemberList() {
        return memberRepository.pagingMemberList().stream()
                .map(MemberDto::new)
                .toList();
    }

    @Override
    public AggregationDto aggregationList() {
        return memberRepository.aggregationList();
    }

    @Override
    public List<MemberTeamDto> basicJoin() {
        return memberRepository.basicJoin().stream()
                .map(MemberTeamDto::new)
                .toList();
    }

    @Override
    public List<MemberTeamDto> joinOn() {
        return memberRepository.joinOn();
    }

    @Override
    public List<MemberTeamDto> unrelatedJoin() {
        return memberRepository.unrelatedJoin();
    }

    @Override
    public MemberTeamDto fetchJoin() {
        Member result = memberRepository.fetchJoin();

        return new MemberTeamDto(result);
    }

    @Override
    public List<MemberDto> whereSubQuery() {
        List<Member> results = memberRepository.whereSubQuery();

        return results.stream()
                .map(MemberDto::new)
                .toList();
    }

    @Override
    public List<MemberAvgDto> selectSubQuery() {
        return memberRepository.selectSubQuery();
    }

    public List<MemberAgeCategoryDto> caseExmaple() {
        return memberRepository.caseExample();
    }

    public List<String> addString() {
        return memberRepository.addString();
    }

    public List<MemberTeamDto> queryProjections() {
        return memberRepository.queryProjections();
    }

    public List<MemberTeamDto> search(MemberSearchCondition memberSearchCondition) {
        return memberRepository.search(memberSearchCondition);
    }

    public Page<MemberTeamDto> searchPageComplex(MemberSearchCondition condition, Pageable pageable) {
        return memberRepository.searchPageComplex(condition, pageable);
    }
}
