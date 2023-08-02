package study.querydsl.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import study.querydsl.dto.ProjectMemberDto;
import study.querydsl.entity.MemberProject;

public interface MemberProjectRepository extends JpaRepository<MemberProject, Long>, MemberProjectRepositoryCustom {
    Page<ProjectMemberDto> pagingMtoM(Pageable pageable);
}
