package study.querydsl.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import study.querydsl.dto.ProjectMemberDto;

import java.util.List;

public interface MemberProjectService {
    Page<ProjectMemberDto> pagingMtoM(Pageable pageable);
}
