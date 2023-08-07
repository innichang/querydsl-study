package study.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.entity.Member;
import study.querydsl.entity.QMember;

@SpringBootTest
@Transactional
class QuerydslApplicationTests {

	@Autowired
	EntityManager em;

	@Test
	void contextLoads() {
        Member member = new Member();
		em.persist(member);

		JPAQueryFactory query = new JPAQueryFactory(em);
		QMember qMember = new QMember("m");

		Member result = query
				.selectFrom(qMember)
				.fetchOne();

		//qFile testing
		Assertions.assertThat(result).isEqualTo(member);

		//Lombok testing
		Assertions.assertThat(result.getId()).isEqualTo(member.getId());
	}

}
