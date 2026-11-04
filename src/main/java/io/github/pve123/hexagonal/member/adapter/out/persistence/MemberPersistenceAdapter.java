package io.github.pve123.hexagonal.member.adapter.out.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.github.pve123.hexagonal.member.application.port.in.GetMembersUseCase.SearchCondition;
import io.github.pve123.hexagonal.member.application.port.out.MemberCommandPort;
import io.github.pve123.hexagonal.member.application.port.out.MemberQueryPort;
import io.github.pve123.hexagonal.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static io.github.pve123.hexagonal.member.adapter.out.persistence.QMemberJpaEntity.memberJpaEntity;

/** 회원 출구 포트를 JPA와 QueryDSL로 구현합니다. 트랜잭션은 이 클래스를 부르는 서비스가 엽니다. */
@Component
@RequiredArgsConstructor
class MemberPersistenceAdapter implements MemberQueryPort, MemberCommandPort {

    private final MemberJpaRepository memberJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Member> search(SearchCondition condition, Pageable pageable) {
        List<Member> content = queryFactory
                .selectFrom(memberJpaEntity)
                .where(MemberSearchConditions.where(condition))
                .orderBy(MemberSearchConditions.orderBy(pageable.getSort()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch()
                .stream()
                .map(MemberJpaEntity::toDomain)
                .toList();

        return PageableExecutionUtils.getPage(content, pageable, () -> queryFactory
                .select(memberJpaEntity.count())
                .from(memberJpaEntity)
                .where(MemberSearchConditions.where(condition))
                .fetchOne());
    }

    @Override
    public Optional<Member> findActiveById(String memberId) {
        return memberJpaRepository.findByIdAndIsDeletedFalse(memberId).map(MemberJpaEntity::toDomain);
    }

    @Override
    public Optional<Member> findById(String memberId) {
        return memberJpaRepository.findById(memberId).map(MemberJpaEntity::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return memberJpaRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByEmailOfOtherMember(String email, String memberId) {
        return memberJpaRepository.existsByEmailAndIdNot(email, memberId);
    }

    @Override
    public Member save(Member member) {
        MemberJpaEntity entity = memberJpaRepository.findById(member.getId())
                .map(found -> {
                    found.apply(member);
                    return found;
                })
                .orElseGet(() -> MemberJpaEntity.from(member));
        // 생성·수정 시각은 DB에 쓰는 순간 채워지므로 바로 내보내서 응답에 담습니다.
        return memberJpaRepository.saveAndFlush(entity).toDomain();
    }
}
