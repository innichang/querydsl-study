package study.querydsl.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * study.querydsl.dto.QMemberDto is a Querydsl Projection type for MemberDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QMemberDto extends ConstructorExpression<MemberDto> {

    private static final long serialVersionUID = 1356709634L;

    public QMemberDto(com.querydsl.core.types.Expression<? extends study.querydsl.entity.Member> member) {
        super(MemberDto.class, new Class<?>[]{study.querydsl.entity.Member.class}, member);
    }

}

