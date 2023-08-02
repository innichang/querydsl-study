package study.querydsl.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import study.querydsl.dto.ProjectMemberDto;
import study.querydsl.repository.MemberProjectRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberProjectServiceImpl implements MemberProjectService {
    private final MemberProjectRepository memberProjectRepository;

    public Page<ProjectMemberDto> pagingMtoM(Pageable pageable) {
        return memberProjectRepository.pagingMtoM(pageable);
    }
}
