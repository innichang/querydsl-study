package study.querydsl.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import study.querydsl.dto.ProjectMemberDto;
import study.querydsl.service.MemberProjectService;

@RestController
@Slf4j
@RequiredArgsConstructor
public class MemberProjectController {

    private final MemberProjectService memberProjectService;
    @GetMapping("/mp/all")
    public Page<ProjectMemberDto> pagingMtoM(Pageable pageable) {

        return memberProjectService.pagingMtoM(pageable);
    }
}
