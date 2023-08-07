package study.querydsl.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import study.querydsl.dto.ProjectMemberDto;

public interface MemberProjectRepositoryCustom {
    Page<ProjectMemberDto> pagingMtoM(Pageable pageable);
}
